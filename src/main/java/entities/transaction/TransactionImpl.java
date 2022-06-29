package entities.transaction;

import enums.TransactionType;
import yahoofinance.Stock;

import java.util.Date;

public class TransactionImpl implements Transaction{
    private final long userID;
    private final Stock stock;
    private final double price;
    private final int count;
    private final Date date;
    private final TransactionType type;

    public TransactionImpl(long userID, Stock stock, int count, double price, TransactionType type) {
        this.userID = userID;
        this.stock = stock;
        this.price = price;
        this.type = type;
        this.count = count;
        this.date = new Date();
    }

    @Override
    public long getUserID() {
        return userID;
    }

    @Override
    public Stock getStock() {
        return stock;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public TransactionType getType() {
        return type;
    }

    @Override
    public int getCount() {
        return count;
    }
}
