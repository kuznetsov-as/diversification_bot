package enums;

public enum CommandName {
    BALANCE("\uD83D\uDCB0 Баланс"), BUY("\uD83D\uDCE5 Купить активы"), PORTFOLIO("\uD83D\uDCBC Портфель акций"),
    SELL("\uD83D\uDCE4  Продать активы"), MARKET("\uD83D\uDCCA Маркет"), GET_QUOTE("\uD83D\uDCC8 Инфо об акции"),
    BUYVIP("⚡VIP аккаунт"), TRANSACTIONS("\uD83D\uDDC4 Транзакции"), START("/start"),
    UNKNOWN("/unknown"), ADD_QUOTE("\uD83C\uDFE6 Добавить акцию"), INCREASE_BALANCE("\uD83C\uDFB0 Пополнить баланс");

    public final String label;

    CommandName(String label) {
        this.label = label;
    }
}
