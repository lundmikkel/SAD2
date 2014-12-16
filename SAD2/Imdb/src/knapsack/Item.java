package knapsack;

public class Item{
    public final int weight;
    public final int profit;
    public final String name;

    public Item(String name, int weight, int profit)
    {
        this.name = name;
        this.weight = weight;
        this.profit = profit;
    }

    @Override
    public String toString() {
        return name;
    }
}
