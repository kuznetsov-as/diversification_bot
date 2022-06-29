package db.exceptions;

import java.sql.SQLException;

public class SQLNoDataFoundException extends Exception {
    public SQLNoDataFoundException(String reason) {
        super(reason);
    }
}
