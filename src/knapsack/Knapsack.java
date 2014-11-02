package knapsack;

import java.util.*;

/**
 * Created by Rasmus on 31-10-2014.
 */
public class Knapsack {
    public static void main(String[] args)
    {
        List<Item> items = new ArrayList<Item>();
        items.add(new Item("1", 1,2)); //X
        items.add(new Item("2", 1,2)); //X
        items.add(new Item("3", 2,4)); //X
        items.add(new Item("4", 2,3));
        //items.add(new Item(2,5));

        int W = 4;
        System.out.println("Input: W = " + W);
        for (Item i : items)
        {
            System.out.println(i + " : w: " + i.getWeight() + " v: " + i.getValue());
        }
        System.out.println();

        Set<Knapsackable> result = knapsack(items, W);
        System.out.println("Result:");
        for (Knapsackable i : result)
        {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    public static Set<Knapsackable> knapsack(List<? extends Knapsackable> items, int W)
    {
        int[][] cache = new int[items.size()+1][W+1];
        for (int item = 1; item <= items.size(); item++){
            for (int w = 0; w <= W; w++)
            {
                Knapsackable i = items.get(item-1);
                if (i.getWeight() > w) continue;
                cache[item][w] = Math.max(cache[item-1][w], i.getValue() + cache[item-1][w-i.getWeight()]);
            }
        }
        /*
        for (int item = 0; item <= items.size(); item++)
        {
            int w = 0, v = 0;
            if (item != 0)
            {
                w = items.get(item-1).weight;
                v = items.get(item-1).value;
            }
            System.out.println("item: " + item + "(w: " + w +" v: " + v + ") = " + Arrays.toString(cache[item]));
        }
        */
        Set<Knapsackable> result = new HashSet<Knapsackable>();
        int w = W;
        for (int item = items.size(); item > 0; item--)
        {
            if (cache[item-1][w-items.get(item-1).getWeight()] == cache[item][w]-items.get(item-1).getValue()) {
                w -= items.get(item - 1).getWeight();
                result.add(items.get(item - 1));
            }
        }
        return result;
    }
}
