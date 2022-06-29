package commands.impl;

import DiversificationBot.DiversificationBot;
import commands.replyCommand.ReplyCommand;
import commands.replyCommand.ReplyCommandAnnotation;
import commands.command.Command;
import commands.command.CommandAnnotation;
import enums.CommandName;
import enums.UserState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import repository.Repository;
import yahoofinance.Stock;

import java.io.IOException;

@ReplyCommandAnnotation(name = UserState.WAITING_QUOTE_COMMAND, description = "Send quote price to user")
@CommandAnnotation(name = CommandName.GET_QUOTE, description = "Show user's balance")
public class QuoteCommand extends Command implements ReplyCommand {
    private static final String urlPrefix = "https://finance.yahoo.com/quote/";

    public QuoteCommand(Update update) {
        super(update);
    }

    @Override
    public SendMessage handleReply(UserState userState, String response) {
        DiversificationBot.Repository.setUserState(getChatID(), UserState.DEFAULT);
        String text;
        try {
            text = stockToString(DiversificationBot.Repository.getQuote(response));
        } catch (IOException e) {
            text = Repository.Mock;
        }
        return newMessage().setText(text).disableWebPagePreview();
    }

    @Override
    public SendMessage execute() {
        DiversificationBot.Repository.setUserState(getChatID(), UserState.WAITING_QUOTE_COMMAND);
        var user = DiversificationBot.Repository.getUser(getChatID());
        var message = newMessage().setText("Выбери акцию");
        var keyboard = DiversificationBot.keyboardFac.buildAllStocksKeyboard(user);
        return message.setReplyMarkup(keyboard);
    }

    private String stockToString(Stock stock) {
        return String.format("%s (%s)\r\n%s\r\n%s", stock.getName(), stock.getCurrency(), stock.getQuote(),
                urlPrefix + stock.getSymbol());
    }
}
