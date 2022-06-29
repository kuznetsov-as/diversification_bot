package commands.command;

import commands.replyCommand.ReplyCommand;
import commands.replyCommand.ReplyCommandAnnotation;
import commands.replyCommand.ReplyCommandAnnotationContainer;
import enums.UserState;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

public class CommandsManager {
    private static final Map<String, Class<? extends Command>> commands = new HashMap<>();
    private static final Map<UserState, Class<? extends ReplyCommand>> replyCommands = new HashMap<>();
    private static final Reflections reflections = new Reflections("commands.impl");

    public static void buildCommands() {
        var commandClasses = reflections.getSubTypesOf(Command.class);
        for (var command : commandClasses) {
            if (command.isAnnotationPresent(CommandAnnotation.class)) {
                var annotation = command.getDeclaredAnnotation(CommandAnnotation.class);
                commands.put(annotation.name().label, command);
            }
        }

    }

    public static void buildReplyCommands() {
        var replyClasses = reflections.getSubTypesOf(ReplyCommand.class);
        for (var reply : replyClasses) {
            if (reply.isAnnotationPresent(ReplyCommandAnnotation.class)) {
                var annotation = reply.getDeclaredAnnotation(ReplyCommandAnnotation.class);
                replyCommands.put(annotation.name(), reply);
            } else if (reply.isAnnotationPresent(ReplyCommandAnnotationContainer.class)) {
                var annotation = reply.getDeclaredAnnotation(ReplyCommandAnnotationContainer.class);
                for (var e : annotation.value()) {
                    replyCommands.put(e.name(), reply);
                }
            }
        }
    }

    public static Class<? extends Command> getCommand(String commandName) {
        if (!commands.containsKey(commandName))
            return commands.get("/unknown");
        return commands.get(commandName);
    }

    public static Class<? extends ReplyCommand> getReplyCommand(UserState userState) {
        return replyCommands.get(userState);
    }
}

