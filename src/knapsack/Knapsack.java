package knapsack;

import java.util.*;

/**
 * Created by Rasmus on 31-10-2014.
 */
public class Knapsack {
    public static void main(String[] args)
    {
        List<Item> items = new ArrayList<Item>();
                          //id ,W,v
        items.add(new Item("1", 1,2));
        items.add(new Item("2", 1,2));
        items.add(new Item("3", 2,4));
        items.add(new Item("4", 1,2));
        items.add(new Item("5", 1,2));
        items.add(new Item("6", 2,4));
        items.add(new Item("7", 1,2));
        items.add(new Item("8", 1,2));
        items.add(new Item("9", 2,4));
        items.add(new Item("10", 2,3));

        int W = 9;
        System.out.println("Input: W = " + W);
        for (Item i : items)
            System.out.println(i + " : w: " + i.getWeight() + " v: " + i.getValue());
        System.out.println();

        Set<Knapsackable> result = knapsack(items, W);
        System.out.println("Result:");
        for (Knapsackable i : result)
            System.out.print(i + " ");
        System.out.println();
    }

    public static Set<Knapsackable> knapsack(List<? extends Knapsackable> items, int W)
    {
        System.out.println("Allocating of size: " + ((W+1) * (items.size()+1)));
        double[][] cache = new double[items.size()+1][W+1];
        System.out.println("Array allocated of size: " + ((W+1) * (items.size()+1)));
        for (int item = 1; item <= items.size(); item++){
            for (int w = 0; w <= W; w++)
            {
                Knapsackable i = items.get(item-1);
                if (i.getWeight() > w) continue;
                cache[item][w] = Math.max(cache[item-1][w], i.getValue() + cache[item-1][w-i.getWeight()]);
            }
        }
        /*for (int item = 0; item <= items.size(); item++)
        {
            if (item == 0) {
                System.out.println("Item w: - v: - : " + Arrays.toString(cache[item]));
                continue;
            }
            Knapsackable k = items.get(item-1);
            System.out.print("Item w: " + k.getWeight() + " v: " + k.getValue() + " : ");
            System.out.println(Arrays.toString(cache[item]));
        }
        System.out.println();*/
        Set<Knapsackable> result = new HashSet<>();
        for (int item = items.size(), w = W; item > 0 && w > 0; item--)
        {
            if (cache[item-1][w-items.get(item-1).getWeight()] == cache[item][w]-items.get(item-1).getValue()) {
                w -= items.get(item - 1).getWeight();
                result.add(items.get(item - 1));
            }
        }
        return result;
    }
}
