package commands.impl;

import commands.replyCommand.ReplyCommand;
import commands.replyCommand.ReplyCommandAnnotation;
import DiversificationBot.DiversificationBot;
import commands.command.Command;
import commands.command.CommandAnnotation;
import entities.transaction.TransactionImpl;
import enums.CommandName;
import enums.Currency;
import enums.UserState;
import enums.TransactionType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import yahoofinance.Stock;

import java.io.IOException;


@ReplyCommandAnnotation(name = UserState.WAITING_SELL_CHOOSE_COUNT, description = "bla")
@ReplyCommandAnnotation(name = UserState.WAITING_SELL_COMMAND, description = "blabla")
@CommandAnnotation(name = CommandName.SELL, description = "sell command")
public class SellCommand extends Command implements ReplyCommand {
    public SellCommand(Update update) {
        super(update);
    }

    @Override
    public SendMessage handleReply(UserState userState, String response) {
        return switch (userState) {
            case WAITING_SELL_CHOOSE_COUNT -> handleSelectCount(response);
            case WAITING_SELL_COMMAND -> handleSell(response);
            default -> newMessage().setText("Critical error");
        };
    }

    @Override
    public SendMessage execute() {
        DiversificationBot.Repository.setUserState(getChatID(), UserState.WAITING_SELL_CHOOSE_COUNT);
        var user = DiversificationBot.Repository.getUser(getChatID());
        var message = newMessage();
        if (user.getPortfolio().keySet().size() == 0) {
            DiversificationBot.Repository.setUserState(getChatID(), UserState.DEFAULT);
            return message.setText("У тебя пустое портфолио");
        }
        var keyboard = DiversificationBot.keyboardFac.buildUserStocksKeyboard(user);
        return message.setText("Выбери акцию").setReplyMarkup(keyboard);
    }

    private SendMessage handleSelectCount(String quoteName) {
        var repository = DiversificationBot.Repository;
        repository.setUserState(getChatID(), UserState.WAITING_SELL_COMMAND);
        repository.getUser(getChatID()).previousReplies.set(0, quoteName);
        var keyboard = DiversificationBot.keyboardFac.buildNumberKeyboard();
        var user = DiversificationBot.Repository.getUser(getChatID());
        var count = user.getPortfolio().get(quoteName);
        if (count == null) count = 0;
        return newMessage().setText(String.format("Таких активов у тебя %d. Сколько хочешь продать?", count))
                .setReplyMarkup(keyboard);
    }

    private SendMessage handleSell(String response) {
        var repository = DiversificationBot.Repository;
        repository.setUserState(getChatID(), UserState.DEFAULT);
        var strStock = repository.getUser(getChatID()).previousReplies.get(0);
        var user = repository.getUser(getChatID());
        var count = Integer.parseInt(response);
        Stock stock;
        try {
            stock = repository.getQuote(strStock);
        } catch (IOException e) {
            e.printStackTrace();
            return newMessage().setText(repository.Mock);
        }
        var price = stock.getQuote().getPrice().doubleValue();
        var T = new TransactionImpl(getChatID(), stock, count, price, TransactionType.SELL);
        var result = repository.proceedTransaction(T);
        var currSymbol = Currency.valueOf(stock.getCurrency()).label;
        if (result)
            return newMessage().setText(String.format("Успешная продажа %d (%s) за %.2f%s\nСумма сделки: %.2f%s\n\n%s",
                    count, strStock, price, currSymbol, price * count, currSymbol, user.toStringBalance())).enableMarkdown(true);
        else
            return newMessage().setText("У тебя нет столько акций");

    }
}
