package repository;

import entities.User;
import entities.transaction.Transaction;
import enums.UserState;
import yahoofinance.Stock;
import yahoofinance.quotes.stock.StockQuote;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class TestRepository implements Repository {

    private static final HashMap<Long, User> users = new HashMap<>();
    private static final HashMap<Long, UserState> states = new HashMap<>();
    private static final Map<String, Stock> quotes = new HashMap<>() {{
        var q = new StockQuote("AAPL");
        q.setPrice(BigDecimal.valueOf(100));
        var a = new Stock("AAPL");
        a.setQuote(q);
        put("AAPL", a);
    }};

    @Override
    public Stock getQuote(String quoteName) {
        return quotes.get("AAPL");
    }

    @Override
    public Collection<Stock> getQuotes(long userID) {
        return quotes.values();
    }

    @Override
    public User createUser(long userID) {
        if (users.containsKey(userID))
            return null;

        users.put(userID, new User(userID));
        states.put(userID, UserState.DEFAULT);
        return users.get(userID);
    }

    @Override
    public User getUser(long userID) {
        var user = users.get(userID);
        if (user == null)
            user = createUser(userID);
        return user;
    }

    @Override
    public void setUserState(long ID, UserState userState) {
        states.put(ID, userState);
    }

    @Override
    public UserState getUserState(long ID) {
        return states.get(ID);
    }

    @Override
    public void increaseUserBalance(User user) {

    }

    @Override
    public boolean proceedTransaction(Transaction transaction) {
        var f = new SimpleDateFormat(
                "yyyy-MM-dd kk:mm:ss");
        System.out.println(f.format(transaction.getDate()));
        var stock = transaction.getStock();
        var count = transaction.getCount();
        var price = transaction.getPrice();
        switch (transaction.getType()) {
            case BUY -> {
                return getUser(transaction.getUserID()).buyStock(stock, count, price);
            }
            case SELL -> {
                return getUser(transaction.getUserID()).sellStock(stock, count, price);
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public String getTransactionHistory(long userID) {
        return null;
    }

    @Override
    public void addExtraQuoteToUser(User user, String quote) {

    }
}
