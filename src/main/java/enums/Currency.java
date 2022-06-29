package enums;

public enum Currency {
    RUB("₽"), USD("$"), EUR("€");
    public final String label;

    Currency(String label) {
        this.label = label;
    }
}
