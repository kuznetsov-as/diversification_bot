package commands.impl;

import DiversificationBot.DiversificationBot;
import commands.command.Command;
import commands.command.CommandAnnotation;
import enums.CommandName;
import enums.Currency;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import repository.Repository;
import yahoofinance.Stock;

import java.io.IOException;
import java.util.*;

@CommandAnnotation(name = CommandName.MARKET, description = "Show user's balance")
public class MarketCommand extends Command {
    public MarketCommand(Update update) {
        super(update);
    }

    @Override
    public SendMessage execute() {
        var message = newMessage();
        String text;
        try {
            text = quotesToString(DiversificationBot.Repository.getQuotes(getChatID()));
        } catch (IOException e) {
            text = Repository.Mock;
        }

        return message.setText(text).enableMarkdown(true);
    }

    private String quotesToString(Collection<Stock> quotes) {
        var sb = new StringBuilder();
        var a = new ArrayList<>(quotes);
        a.sort(new Comparator<Stock>() {
            @Override
            public int compare(Stock stock, Stock t1) {
                return stock.getSymbol().compareTo(t1.getSymbol());
            }
        });
        for (var q : a) {
            sb.append(String.format("*%s*: %.2f%s (%s)\n", q.getSymbol(), q.getQuote().getPrice(),
                    Currency.valueOf(q.getCurrency()).label, q.getName()));
        }
        return sb.toString();
    }
}
