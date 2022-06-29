package enums;

import java.util.ArrayList;

public enum BaseStock {
    AAPL, GOOGL, AMD, IBM, INTC, BABA, TSLA, AMZN, FB, V, MSFT, NVDA, MA, NFLX, KO, DIS, BAC, F, SPOT, HPQ, NOK, TWTR,
    UBER, APA, PINS, ZM, CSCO, QCOM, JNJ, PEP;

    public static ArrayList<String> getNames() {
        var l = new ArrayList<String>();
        for (var e : BaseStock.values())
            l.add(e.toString());
        return l;
    }
}
