package commands.replyCommand;


import enums.UserState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


public interface ReplyCommand {
    SendMessage handleReply(UserState userState, String response);
}
