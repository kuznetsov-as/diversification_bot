package commands.command;

import DiversificationBot.DiversificationBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public abstract class Command {
    public abstract SendMessage execute();

    private final long chatID;

    public long getChatID() {
        return chatID;
    }

    protected Command(Update update) {
        this.chatID = update.getMessage().getChatId();
    }

    public SendMessage newMessage() {
        ReplyKeyboardMarkup menuKeyboard;
        if(DiversificationBot.Repository.getUser(chatID).isVip)
            menuKeyboard = DiversificationBot.keyboardFac.buildVipMainMenu();
        else
            menuKeyboard = DiversificationBot.keyboardFac.buildMainMenu();

        return new SendMessage().setChatId(chatID).setReplyMarkup(menuKeyboard);
    }
}
