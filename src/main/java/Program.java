import DiversificationBot.DiversificationBot;
import commands.command.CommandsManager;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Program{
    public static void main(String[] args) {
        CommandsManager.buildCommands();
        CommandsManager.buildReplyCommands();
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new DiversificationBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

