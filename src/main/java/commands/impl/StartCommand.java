package commands.impl;

import commands.command.Command;
import commands.command.CommandAnnotation;
import enums.CommandName;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@CommandAnnotation(name = CommandName.START, description = "register user")
public class StartCommand extends Command {
    public StartCommand(Update update) {
        super(update);
    }

    @Override
    public SendMessage execute() {
        return newMessage().setText("\uD83D\uDCB8 Привет! Я Добрый Брокер \uD83D\uDCB8\n\n" +
                "Твой стартовый капитал: 5000$ и 50000₽\n\n" +
                "Можешь начать с команды \"\uD83D\uDCCA Маркет\", чтобы посмотреть доступные акции\n\n" +
                "\uD83D\uDD34 Купи акции\n\n" +
                "\uD83D\uDD34 Немного подожди\n\n" +
                "\uD83D\uDD34 Продай, когда их цена будет на пике\n\n" +
                "Удачи в бою :)");

    }
}
