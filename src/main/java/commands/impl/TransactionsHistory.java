package commands.impl;

import DiversificationBot.DiversificationBot;
import commands.command.Command;
import commands.command.CommandAnnotation;
import enums.CommandName;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@CommandAnnotation(name= CommandName.TRANSACTIONS)
public class TransactionsHistory extends Command {
    public TransactionsHistory(Update update) {
        super(update);
    }

    @Override
    public SendMessage execute() {
        var text = DiversificationBot.Repository.getTransactionHistory(getChatID());
        return newMessage().setText(text);
    }
}
