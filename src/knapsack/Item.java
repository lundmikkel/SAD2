package knapsack;

public class Item implements Knapsackable{
    public final int weight, value;
    public final String name;

    public Item(String name, int weight, int value)
    {
        this.name = name;
        this.weight = weight;
        this.value = value;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public double getValue() {
        return value;
    }
}
