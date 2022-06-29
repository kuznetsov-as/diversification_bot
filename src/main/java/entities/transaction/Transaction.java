package entities.transaction;

import enums.TransactionType;
import yahoofinance.Stock;

import java.util.Date;

public interface Transaction {
    long getUserID();

    Stock getStock();

    double getPrice();

    Date getDate();

    TransactionType getType();

    int getCount();
}
