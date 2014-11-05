package knapsack;

public class Item{
    private final int weight;
    private final int value;
    private final String name;

    public Item(String name, int weight, int value)
    {
        this.name = name;
        this.weight = weight;
        this.value = value;
    }

    @Override
    public String toString() {
        return getName();
    }

    public int getWeight() {
        return weight;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
