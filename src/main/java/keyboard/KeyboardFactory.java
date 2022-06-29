package keyboard;

import entities.User;
import enums.CommandName;
import enums.BaseStock;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KeyboardFactory {
    private final static List<String> chooseQuoteCountNums = Arrays.asList("1", "2", "5", "10", "25", "50");
    private final static List<String> mainMenuButtons = Arrays.asList(CommandName.BALANCE.label, CommandName.BUY.label,
            CommandName.PORTFOLIO.label, CommandName.SELL.label, CommandName.MARKET.label, CommandName.GET_QUOTE.label,
            CommandName.BUYVIP.label, CommandName.TRANSACTIONS.label);
    private final static List<String> VipMainMenuButtons = initVipMenuButtons();

    public ReplyKeyboardMarkup buildAllStocksKeyboard(User user) {
        var extraQuotes = new ArrayList<String>();
        if (user.isVip)
            extraQuotes = new ArrayList<String>(user.getExtraQuotes());
        var stocks = BaseStock.getNames();
        stocks.addAll(extraQuotes);
        Collections.sort(stocks);
        return buildKeyboardFromArr(stocks, 6);
    }

    public ReplyKeyboardMarkup buildUserStocksKeyboard(User user) {
        var portfolio = new ArrayList<>(user.getPortfolio().keySet());
        Collections.sort(portfolio);
        return buildKeyboardFromArr(portfolio, 6);
    }

    public ReplyKeyboardMarkup buildNumberKeyboard() {
        return buildKeyboardFromArr(chooseQuoteCountNums, 2);
    }

    public ReplyKeyboardMarkup buildMainMenu() {
        return buildKeyboardFromArr(mainMenuButtons, 2);
    }

    public ReplyKeyboardMarkup buildVipMainMenu(){
        return buildKeyboardFromArr(VipMainMenuButtons, 2);
    }

    private ReplyKeyboardMarkup buildKeyboardFromArr(List<? extends Object> arr, int columnCount) {
        var keyboardMarkup = new ReplyKeyboardMarkup();
        var keyboard = new ArrayList<KeyboardRow>();
        for (var row = 0; row < arr.size(); row += columnCount) {
            var keyboardRow = new KeyboardRow();
            for (var i = row; i < row + columnCount && i < arr.size(); i++) {
                keyboardRow.add(arr.get(i).toString());
            }
            keyboard.add(keyboardRow);
        }
        return keyboardMarkup.setKeyboard(keyboard).setResizeKeyboard(false);
    }

    private static ArrayList<String> initVipMenuButtons(){
        var l = new ArrayList<>(mainMenuButtons);
        l.add(CommandName.INCREASE_BALANCE.label);
        l.add(CommandName.ADD_QUOTE.label);
        return l;
    }
}
