package commands.impl;

import commands.command.Command;
import commands.command.CommandAnnotation;
import enums.CommandName;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@CommandAnnotation(name = CommandName.BUYVIP)
public class BuyVip extends Command {
    public BuyVip(Update update) {
        super(update);
    }

    @Override
    public SendMessage execute() {
        return newMessage().setText("⚡️️До конца 2020 года стоимость VIP аккаунта 29₽\n\n" +
                "Привелегии VIP:\n\n\uD83D\uDD34 Полный доступ к RUB кошельку\n\n\uD83D\uDD34 Добавление любых " +
                "существующих акций\n\n\uD83D\uDD34 Неограниченное пополнение баланса\n\n" +
                "Писать сюда @igorplyuhin\n\nМы очень благодарны за любую " +
                "вашу поддрежку, приобретая VIP вы стимулируете нас на дальнейшее развитие проекта.\uD83D\uDE18");
    }
}
