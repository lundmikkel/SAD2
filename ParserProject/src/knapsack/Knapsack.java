package knapsack;

import java.util.*;

public class Knapsack {
    public static void main(String[] args) {
        List<Item> items = new ArrayList<>();
        //                  id,  W, v
        items.add(new Item(" 1", 1, 2));
        items.add(new Item(" 2", 1, 2));
        items.add(new Item(" 3", 2, 4));
        items.add(new Item(" 4", 1, 2));
        items.add(new Item(" 5", 1, 2));
        items.add(new Item(" 6", 2, 4));
        items.add(new Item(" 7", 1, 2));
        items.add(new Item(" 8", 1, 2));
        items.add(new Item(" 9", 2, 4));
        items.add(new Item("10", 2, 3));

        int W = 9;
        System.out.println("Input: W = " + W);
        for (Item i : items)
            System.out.println(i + " : w: " + i.getWeight() + " v: " + i.getValue());
        System.out.println();

        Set<Item> result = knapsack(items, W, 0.1, new KnapsackHelper<Item>() {
            @Override
            public int getWeight(Item item) {
                return item.getWeight();
            }

            @Override
            public double getValue(Item item) {
                return item.getValue();
            }
        });
        System.out.println("Result:");
        for (Item i : result)
            System.out.print(i + " ");
        System.out.println();
    }

    public static <T> Set<T> knapsack(List<T> items, int W, double scalingFactor, KnapsackHelper<T> knapsackHelper) {
        //int maxWeight = items.stream().mapToInt(i -> i.getWeight()).max().getAsInt();
        //double scalingFactor = precision * maxWeight / items.size();
        W = (int)Math.floor(W/scalingFactor);

        System.out.printf(Locale.US, "Allocating of size: %,d Byte\n", 4 * (W + 1) * (items.size() + 1));

        double[][] cache = new double[items.size() + 1][W + 1];
        for (int item = 1, size = items.size(); item <= size; ++item) {
            for (int w = 0; w <= W; ++w) {
                T i = items.get(item - 1);
                int wi = (int) Math.ceil(knapsackHelper.getWeight(i)/scalingFactor);
                if (wi <= w) {
                    double notSelected = cache[item - 1][w];
                    double selected = knapsackHelper.getValue(i) + cache[item - 1][w - wi];
                    cache[item][w] = Math.max(notSelected, selected);
                }
            }
        }

        //for (int item = 0; item <= items.size(); item++)
        //{
        //if (item == 0) {
        //System.out.println("Item w: - v: - : " + Arrays.toString(cache[item]));
        //continue;
        //}
        //Knapsackable k = items.get(item-1);
        //System.out.print("Item w: " + k.getWeight() + " v: " + k.getValue() + " : ");
        //System.out.println(Arrays.toString(cache[item]));
        //}
        //System.out.println();

        Set<T> result = new HashSet<>();
        for (int item = items.size(), w = W; 0 < item; --item) {
            int wi = (int) Math.ceil(knapsackHelper.getWeight(items.get(item - 1)) / scalingFactor);
            if (wi <= w) {
                double actual = cache[item - 1][w - wi];
                double expected = cache[item][w] - knapsackHelper.getValue(items.get(item - 1));
                if (actual == expected) {
                    w -= wi;
                    result.add(items.get(item - 1));
                }
            }
        }
        return result;
    }
}