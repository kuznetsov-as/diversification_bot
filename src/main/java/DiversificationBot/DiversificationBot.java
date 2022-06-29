package DiversificationBot;

import commands.command.CommandsManager;
import db.DBController;
import enums.UserState;
import keyboard.KeyboardFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import repository.ApiRepository;
import repository.Repository;

import java.lang.reflect.InvocationTargetException;


public class DiversificationBot extends TelegramLongPollingBot {
    public static final Repository Repository = new ApiRepository(new DBController());
    public static final KeyboardFactory keyboardFac = new KeyboardFactory();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                var message = handleUpdate(update);
                execute(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private SendMessage handleUpdate(Update update) throws InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        var userState = Repository.getUserState(update.getMessage().getChatId());
        var userMessage = update.getMessage().getText();
        if (userState != null && userState != UserState.DEFAULT) {
            return CommandsManager.getReplyCommand(userState).getDeclaredConstructor(Update.class)
                    .newInstance(update).handleReply(userState, userMessage);
        } else {
            return CommandsManager.getCommand(userMessage).getDeclaredConstructor(Update.class)
                    .newInstance(update).execute();
        }
    }

    @Override
    public String getBotUsername() {
        return "Bot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("Token");
    }
}