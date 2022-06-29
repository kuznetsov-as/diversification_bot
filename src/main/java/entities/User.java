package entities;


import enums.Currency;
import yahoofinance.Stock;

import java.lang.reflect.Array;
import java.util.*;

public class User {
    public final ArrayList<String> previousReplies = new ArrayList<>() {{
        add("");
    }};
    public final Boolean isVip;
    private final long id;
    private double usdBalance;
    private double rubBalance;
    private final HashMap<String, Integer> portfolio;
    private final HashSet<String> extraQuotes;


    public User(long id, double usdBalance, double rubBalance, HashMap<String, Integer> portfolio, boolean isVip,
                HashSet<String> extraQuotes) {
        this.id = id;
        this.usdBalance = usdBalance;
        this.rubBalance = rubBalance;
        this.portfolio = portfolio;
        this.isVip = isVip;
        this.extraQuotes = extraQuotes;
    }


    public User(long id) {
        this.id = id;
        this.usdBalance = 5000;
        this.rubBalance = 50000;
        this.portfolio = new HashMap<>();
        this.isVip = false;
        this.extraQuotes = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public double getUsdBalance() {
        return usdBalance;
    }

    public double getRubBalance() {
        return rubBalance;
    }

    public HashMap<String, Integer> getPortfolio() {
        return portfolio;
    }

    public String toStringBalance() {
        return String.format("%s Кошелёк: %.2f%s\n%s Кошелёк: %.2f%s", Currency.USD, usdBalance,
                Currency.USD.label,Currency.RUB, rubBalance, Currency.RUB.label);
    }

    public HashSet<String> getExtraQuotes() {
        return extraQuotes;
    }

    public void addExtraQuote(String quote) {
        if (extraQuotes != null)
            extraQuotes.add(quote);
    }

    public boolean buyStock(Stock stock, int count, double price) {
        var symbol = stock.getQuote().getSymbol();
        if (stock.getCurrency().equals(Currency.USD.toString())) {
            if (usdBalance - count * price < 0)
                return false;
            usdBalance -= count * price;
        } else if (stock.getCurrency().equals(Currency.RUB.toString())) {
            if (rubBalance - count * price < 0)
                return false;
            rubBalance -= count * price;
        } else
            return false;

        if (portfolio.containsKey(symbol))
            portfolio.put(symbol, portfolio.get(symbol) + count);
        else
            portfolio.put(symbol, count);

        return true;
    }

    public boolean sellStock(Stock stock, int count, double price) {
        var symbol = stock.getQuote().getSymbol();
        if (!portfolio.containsKey(symbol) || portfolio.get(symbol) < count)
            return false;

        if (stock.getCurrency().equals(Currency.USD.toString()))
            usdBalance += count * price;
        else if (stock.getCurrency().equals(Currency.RUB.toString()))
            rubBalance += count * price;
        else
            return false;

        portfolio.put(symbol, portfolio.get(symbol) - count);
        if (portfolio.get(symbol) == 0)
            portfolio.remove(symbol);
        return true;
    }

    public void increaseBalance(){
        usdBalance += 1000;
        rubBalance += 10000;
    }
}