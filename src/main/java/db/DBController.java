package db;

import db.tables.TransactionsTable;
import db.tables.UsersTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBController {
    private final Connection conn = initConnection();
    public final UsersTable usersTable = new UsersTable(conn);
    public final TransactionsTable transactionsTable = new TransactionsTable(conn);

    public DBController() {
    }
    private Connection initConnection(){
        var pass = System.getenv("BrokerDBPass");
        try {
            return DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/broker_bot", "broker", pass);
        } catch (SQLException e) {
            System.out.println("Connection to DB Failed!!!");
            e.printStackTrace(System.out);
            return null;
        }
    }
}
