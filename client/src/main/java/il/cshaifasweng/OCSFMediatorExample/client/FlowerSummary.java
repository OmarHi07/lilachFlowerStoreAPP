package il.cshaifasweng.OCSFMediatorExample.client;

public class FlowerSummary {
    private final String flowerName;
    private final int ordersCount;
    private final int totalQuantity;
    private final double totalIncome;

    public FlowerSummary(String flowerName, int ordersCount, int totalQuantity, double totalIncome) {
        this.flowerName = flowerName;
        this.ordersCount = ordersCount;
        this.totalQuantity = totalQuantity;
        this.totalIncome = totalIncome;
    }

    public String getFlowerName()  { return flowerName; }
    public int getOrdersCount()    { return ordersCount; }
    public int getTotalQuantity()  { return totalQuantity; }
    public double getTotalIncome() { return totalIncome; }
}
