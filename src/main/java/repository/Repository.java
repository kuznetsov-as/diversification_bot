package repository;

import entities.User;
import entities.transaction.Transaction;
import enums.UserState;
import yahoofinance.Stock;

import java.io.IOException;
import java.util.Collection;

public interface Repository {
    String Mock = "Маркет сейчас недоступен, попробуй позже";
    Stock getQuote(String quoteName) throws IOException;
    Collection<Stock> getQuotes(long userID) throws IOException;
    User createUser(long ID);
    User getUser(long ID);
    void setUserState(long ID, UserState userState);
    UserState getUserState(long ID);
    boolean proceedTransaction(Transaction transaction);
    String getTransactionHistory(long userID);
    void addExtraQuoteToUser(User user, String quote);
    void increaseUserBalance(User user);
}

