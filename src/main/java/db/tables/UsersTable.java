package db.tables;

import db.exceptions.SQLNoDataFoundException;
import entities.User;
import yahoofinance.Stock;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class UsersTable {
    private Connection conn = null;
    private static final String tableName = "users";

    public UsersTable(Connection conn) {
        this.conn = conn;
    }

    public User addUser(User user) throws SQLException {
        var stmt = conn.createStatement();
        var query = String.format("INSERT INTO %s VALUES (%d, %f, '{}', false, '{}', %f);", tableName,
                user.getId(), user.getUsdBalance(), user.getRubBalance());
        stmt.executeUpdate(query);
        stmt.close();
        return user;
    }

    public User getUser(long userId) throws SQLException, SQLNoDataFoundException {
        var stmt = conn.createStatement();
        var query = String.format("SELECT * FROM %s WHERE id=%d;", tableName, userId);
        var rs = stmt.executeQuery(query);
        stmt.close();
        if (!rs.next())
            throw new SQLNoDataFoundException("NO such user");

        var arr = (String[]) rs.getArray("portfolio").getArray();
        var extraQuotes = (String[]) rs.getArray("extra_quotes").getArray();
        var set = new HashSet<>(Arrays.asList(extraQuotes));
        return new User(rs.getInt("id"), rs.getDouble("usd_balance"), rs.getDouble("rub_balance"),
                arr2HashMap(arr), rs.getBoolean("isVip"), set);
    }

    public void updateUser(User user) throws SQLException {
        var stmt = conn.createStatement();
        var usdBalance = user.getUsdBalance();
        var rubBalance = user.getRubBalance();
        var portfolio = hashMap2String(user.getPortfolio());
        var id = user.getId();
        var extraQuotes = user.getExtraQuotes();
        var query = String.format("UPDATE %s SET usd_balance = %f, rub_balance = %f, portfolio = '%s', extra_quotes = '%s' WHERE id = %d;",
                tableName, usdBalance, rubBalance, portfolio, setToStr(extraQuotes), id);
        stmt.executeUpdate(query);
        stmt.close();
    }

    private HashMap<String, Integer> arr2HashMap(String[] arr) {
        var portfolio = new HashMap<String, Integer>();
        for (var stock : arr) {
            if (portfolio.containsKey(stock))
                portfolio.put(stock, portfolio.get(stock) + 1);
            else
                portfolio.put(stock, 1);
        }
        return portfolio;
    }

    private String hashMap2String(HashMap<String, Integer> map) {
        var sb = new StringBuilder().append('{');
        for (var key : map.keySet()) {
            var count = map.get(key);
            for (var i = 0; i < count; i++) {
                sb.append(String.format("\"%s\",", key));
            }
        }
        if (sb.length() > 1)
            sb.deleteCharAt(sb.length() - 1);
        sb.append('}');
        return sb.toString();
    }

    private String setToStr(HashSet<String> arr){
        var sb = new StringBuilder().append("{");
        for (var e : arr){
            sb.append(String.format("\"%s\",", e));
        }
        if (sb.length() > 1)
            sb.deleteCharAt(sb.length() - 1);
        sb.append('}');
        return sb.toString();
    }
}
