package knapsack;

import java.util.*;

/**
 * Created by Rasmus on 31-10-2014.
 */
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

        Set<Item> result = knapsack(items, W, 0.1);
        System.out.println("Result:");
        for (Item i : result)
            System.out.print(i + " ");
        System.out.println();
    }

    public static <T extends Knapsackable> Set<T> knapsack(List<T> items, int W, double scalingFactor) {
        //int maxWeight = items.stream().mapToInt(i -> i.getWeight()).max().getAsInt();
        //double scalingFactor = precision * maxWeight / items.size();
        W = (int)Math.floor(W/scalingFactor);

        System.out.println("Allocating of size: " + (4 * ((W + 1) * (items.size() + 1))) + " Byte");

        double[][] cache = new double[items.size() + 1][W + 1];
        for (int item = 1, size = items.size(); item <= size; ++item) {
            for (int w = 0; w <= W; ++w) {
                T i = items.get(item - 1);
                int wi = (int) Math.ceil(i.getWeight()/scalingFactor);
                if (wi <= w) {
                    double notSelected = cache[item - 1][w];
                    double selected = i.getValue() + cache[item - 1][w - wi];
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
            int wi = (int) Math.ceil(items.get(item-1).getWeight() / scalingFactor);
            if (wi <= w) {
                double actual = cache[item - 1][w - wi];
                double expected = cache[item][w] - items.get(item - 1).getValue();
                if (actual == expected) {
                    w -= wi;
                    result.add(items.get(item - 1));
                }
            }
        }
        return result;
    }
}
