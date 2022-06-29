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


@ReplyCommandAnnotation(name = UserState.WAITING_BUY_PURCHASE, description = "buy stock")
@ReplyCommandAnnotation(name = UserState.WAITING_BUY_CHOOSE_COUNT, description = "aaa")
@CommandAnnotation(name = CommandName.BUY, description = "buy stock")
public class BuyCommand extends Command implements ReplyCommand {
    public BuyCommand(Update update) {
        super(update);
    }

    @Override
    public SendMessage handleReply(UserState userState, String response) {
        return switch (userState) {
            case WAITING_BUY_CHOOSE_COUNT -> handleSelectCount(response);
            case WAITING_BUY_PURCHASE -> handlePurchase(response);
            default -> newMessage().setText("Critical error");
        };
    }

    @Override
    public SendMessage execute() {
        DiversificationBot.Repository.setUserState(getChatID(), UserState.WAITING_BUY_CHOOSE_COUNT);
        var user = DiversificationBot.Repository.getUser(getChatID());
        var message = newMessage().setText("Выбери акцию");
        var keyboard = DiversificationBot.keyboardFac.buildAllStocksKeyboard(user);
        return message.setReplyMarkup(keyboard);
    }

    private SendMessage handleSelectCount(String quoteName) {
        DiversificationBot.Repository.setUserState(getChatID(), UserState.WAITING_BUY_PURCHASE);
        var user = DiversificationBot.Repository.getUser(getChatID());
        user.previousReplies.set(0, quoteName);
        var keyboard = DiversificationBot.keyboardFac.buildNumberKeyboard();
        Stock stock;
        try {
            stock = DiversificationBot.Repository.getQuote(quoteName);
        } catch (IOException e) {
            e.printStackTrace();
            return newMessage().setText("Выбери количество").setReplyMarkup(keyboard);
        }
        return newMessage().setText(String.format("%s\nСейчас стоит: %.2f%s\n\n%s\nСколько хочешь купить?", stock.getName(),
                stock.getQuote().getPrice(), Currency.valueOf(stock.getCurrency()).label,user.toStringBalance())).setReplyMarkup(keyboard);

    }

    private SendMessage handlePurchase(String strCount) {
        var repository = DiversificationBot.Repository;
        repository.setUserState(getChatID(), UserState.DEFAULT);
        var user = repository.getUser(getChatID());
        var strStock = user.previousReplies.get(0);
        var count = Integer.parseInt(strCount);
        Stock stock;
        try {
            stock = repository.getQuote(strStock);
        } catch (IOException e) {
            e.printStackTrace();
            return newMessage().setText(repository.Mock);
        }
        var price = stock.getQuote().getPrice().doubleValue();
        var t = new TransactionImpl(getChatID(), stock, count, price, TransactionType.BUY);
        var result = repository.proceedTransaction(t);
        var currSymbol = Currency.valueOf(stock.getCurrency()).label;
        if (result)
            return newMessage().setText(String.format("Успешная покупка %d (%s) за %.2f%s\nСумма сделки: %.2f%s\n\nТвой баланс:\n%s",
                    count, strStock, price, currSymbol, price * count, currSymbol, user.toStringBalance())).enableMarkdown(true);
        else
            return newMessage().setText("Недостаточно денег(\n\nКупи VIP аккаунт, чтобы пополнять баланс");
    }
}
