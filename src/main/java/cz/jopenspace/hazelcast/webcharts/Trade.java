package cz.jopenspace.hazelcast.webcharts;

public class Trade {
    private final String symbol;
    private final long price;

    public Trade(String symbol, long price) {
        this.symbol = symbol;
        this.price = price;
    }

    public long getPrice() {
        return price;
    }

    public String getSymbol() {
        return symbol;
    }
}
