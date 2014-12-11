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

        //int[][] items = new int[][]{
        //    new int[]{ 1, 2}, //  1
        //    new int[]{ 1, 2}, //  2
        //    new int[]{ 2, 4}, //  3
        //    new int[]{ 1, 2}, //  4
        //    new int[]{ 1, 2}, //  5
        //    new int[]{ 2, 4}, //  6
        //    new int[]{ 1, 2}, //  7
        //    new int[]{ 1, 2}, //  8
        //    new int[]{ 2, 4}, //  9
        //    new int[]{ 2, 3}, // 10
        //};

        int K = 5;
        int W = 20;
        System.out.println("Input: W = " + W);
        for (Item i : items)
            System.out.println(i + " : w: " + i.getWeight() + " v: " + i.getValue());
        System.out.println();

        Set<Item> result = knapsack(items, K, W, 1, new KnapsackHelper<Item>() {
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

    public static <T> Set<T> knapsack(List<T> itemsList, int K, int W, double scalingFactor, KnapsackHelper<T> knapsackHelper) {
        T[] items = (T[]) itemsList.toArray();
        int N = items.length;
        int[] weights = new int[N];
        for (int i = 0; i < N; ++i)
            weights[i] = (int) Math.ceil(knapsackHelper.getWeight(items[i]) / scalingFactor);
        double[] values = new double[N];
        for (int i = 0; i < N; ++i)
            values[i] = knapsackHelper.getValue(items[i]);

        //int maxWeight = items.stream().mapToInt(i -> i.getWeight()).max().getAsInt();
        //double scalingFactor = precision * maxWeight / items.size();
        W = (int) Math.floor(W / scalingFactor);

        System.out.printf(Locale.US, "Allocating of size: %,d Byte\n", 8 * (N + 1) * (W + 1) * (K + 1));

        double[][][] cache = new double[K + 1][N + 1][W + 1];

        // Iterate each layer
        // k is the current number of items needed picked
        for (int k = 1; k <= K; ++k) {

            // Iterate all items
            for (int i = 1; i <= N; ++i) {

                for (int w = 0; w <= W; ++w) {
                    int wi = weights[i - 1];

                    if (wi <= w) {
                        double notSelected = cache[k][i - 1][w];
                        double selected = cache[k - 1][i - 1][w - wi] + values[i - 1];

                        cache[k][i][w] = Math.max(notSelected, selected);
                    }
                }
            }
        }

        for (int k = 1; k <= K; ++k) {
            System.out.println("k = " + k);
            for (int i = 0; i <= N; i++) {
                if (i == 0) {
                    System.out.println("Item w: - v: - : " + Arrays.toString(cache[k][i]));
                    continue;
                }
                System.out.print("Item w: " + weights[i - 1] + " v: " + values[i - 1] + " : ");
                System.out.println(Arrays.toString(cache[k][i]));
            }
            System.out.println();
        }
        System.out.println();

        Set<T> result = new HashSet<>();
        for (int i = N, w = W, k = K; 0 < k; --i) {
            T item = items[i - 1];
            int wi = (int) Math.ceil(knapsackHelper.getWeight(item) / scalingFactor);

            if (wi <= w) {
                double actual = cache[k - 1][i - 1][w - wi];
                double expected = cache[k][i][w] - knapsackHelper.getValue(item);
                if (actual == expected) {
                    w -= wi;
                    --k;
                    result.add(item);
                }
            }
        }
        return result;
    }
}
