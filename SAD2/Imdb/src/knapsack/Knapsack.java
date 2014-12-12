package knapsack;

import java.util.*;

public class Knapsack {
    public static void main(String[] args) {
        List<Item> items = new ArrayList<>();
        //                  id,  W, v
        items.add(new Item(" 1", 3, 3));
        items.add(new Item(" 2", 3, 3));
        items.add(new Item(" 3", 2, 3));
        items.add(new Item(" 4", 5, 4));

        KnapsackHelper kh = new KnapsackHelper<Item>() {
            @Override
            public int getWeight(Item item) {
                return item.getWeight();
            }

            @Override
            public double getValue(Item item) {
                return item.getValue();
            }
        };

        for (Item i : items)
            System.out.println(i + " : w: " + i.getWeight() + " v: " + i.getValue());
        System.out.println();

        System.out.println("K = 3, W = 8:");
        //knapsack(items, 3, 8, 1, kh).forEach(i -> System.out.print(i + " "));
        knapsackRecursive(items, 3, 8, 1, kh).forEach(i -> System.out.print(i + " "));
        System.out.println("\n");

        System.out.println("K = 2, W = 8:");
        //knapsack(items, 2, 8, 1, kh).forEach(i -> System.out.print(i + " "));
        knapsackRecursive(items, 2, 8, 1, kh).forEach(i -> System.out.print(i + " "));
        System.out.println("\n");

        System.out.println("K = 2, W = 6:");
        //knapsack(items, 2, 6, 1, kh).forEach(i -> System.out.print(i + " "));
        knapsackRecursive(items, 2, 6, 1, kh).forEach(i -> System.out.print(i + " "));
        System.out.println("\n");

        System.out.println("K = 3, W = 6:");
        //knapsack(items, 3, 6, 1, kh).forEach(i -> System.out.print(i + " "));
        knapsackRecursive(items, 3, 6, 1, kh).forEach(i -> System.out.print(i + " "));
        System.out.println("\n");
    }

    public static <T> Set<T> knapsackRecursive(List<T> itemsList, int K, int W, double scalingFactor, KnapsackHelper<T> knapsackHelper) {
        T[] items = (T[]) itemsList.toArray();
        int N = items.length;

        // No possible solution
        if (K > N)
            return new HashSet<>();

        int[] weights = new int[N + 1];
        for (int i = 0; i < N; ++i)
            weights[i] = (int) Math.ceil(knapsackHelper.getWeight(items[i]) / scalingFactor);

        // All items needed
        if (K == N) {
            int w = 0;
            for (int wi : weights)
                w += wi;

            return (w <= W) ? new HashSet<>(itemsList) : new HashSet<>();
        }

        double[] values = new double[N + 1];
        for (int i = 0; i < N; ++i)
            values[i] = knapsackHelper.getValue(items[i]);

        Double[][][] cache = new Double[K + 1][N + 1][W + 1];

        opt(K, N, W, weights, values, cache);

        /*
        for (int k = K; k >= 0; --k) {
            System.out.println("k = " + k);
            for (int i = N; i >= 0; --i) {
                if (i == 0) {
                    System.out.println("Item 0 w: - v: -.- : " + Arrays.toString(cache[k][i]));
                    continue;
                }
                System.out.print("Item " + i + " w: " + weights[i - 1] + " v: " + values[i - 1] + " : ");
                System.out.println(Arrays.toString(cache[k][i]));
            }
            System.out.println();
        }
        System.out.println();
        //*/

        Set<T> result = new HashSet<>();
        for (int i = N, w = W, k = K; 0 < k; --i) {
            // In case there is no solution
            if (i == 0 && k > 0)
                return new HashSet<>();

            int wi = weights[i - 1];

            if (wi <= w) {
                Double actual = cache[k - 1][i - 1][w - wi];
                double expected = cache[k][i][w] - values[i - 1];

                if ((actual == null && expected == 0.0) || (actual != null && actual == expected)) {
                    w -= wi;
                    --k;
                    T item = items[i - 1];
                    result.add(item);
                }
            }
        }

        return result;
    }

    private static <T> double opt(int k, int i, int w, int weights[], double[] values, Double[][][] cache) {
        // Returned cached value
        if (cache[k][i][w] != null)
            return cache[k][i][w];

        if (i == 0 || k == 0)
            return 0;

        int wi = weights[i - 1];

        if (w < wi)
            return opt(k, i - 1, w, weights, values, cache);


        double notSelected = opt(k, i - 1, w, weights, values, cache);
        double selected = opt(k - 1, i - 1, w - wi, weights, values, cache) + values[i - 1];

        // Cache and return value
        return cache[k][i][w] = Math.max(notSelected, selected);
    }

    public static <T> Set<T> knapsack(List<T> itemsList, int K, int W, double scalingFactor, KnapsackHelper<T> knapsackHelper) {
        T[] items = (T[]) itemsList.toArray();
        int N = items.length;

        // No possible solution
        if (K > N)
            return new HashSet<>();

        int[] weights = new int[N];
        for (int i = 0; i < N; ++i)
            weights[i] = (int) Math.ceil(knapsackHelper.getWeight(items[i]) / scalingFactor);

        // All items needed
        if (K == N) {
            int w = 0;
            for (int wi : weights)
                w += wi;

            return (w <= W) ? new HashSet<>(itemsList) : new HashSet<>();
        }

        double[] values = new double[N];
        for (int i = 0; i < N; ++i)
            values[i] = knapsackHelper.getValue(items[i]);

        //int maxWeight = items.stream().mapToInt(i -> i.getWeight()).max().getAsInt();
        //double scalingFactor = precision * maxWeight / items.size();
        W = (int) Math.floor(W / scalingFactor);

        //System.out.printf(Locale.US, "Allocating of size: %,d Byte\n", 8 * (N + 1) * (W + 1) * (K + 1));

        double[][][] cache = new double[K + 1][N + 1][W + 1];

        // Iterate each layer
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

        /*
        for (int k = K; k >= 0; --k) {
            System.out.println("k = " + k);
            for (int i = N; i >= 0; --i) {
                if (i == 0) {
                    System.out.println("Item 0 w: - v: -.- : " + Arrays.toString(cache[k][i]));
                    continue;
                }
                System.out.print("Item " + i + " w: " + weights[i - 1] + " v: " + values[i - 1] + " : ");
                System.out.println(Arrays.toString(cache[k][i]));
            }
            System.out.println();
        }
        System.out.println();
        //*/

        //System.out.println(cache[K][N][W]);

        Set<T> result = new HashSet<>();
        for (int i = N, w = W, k = K; 0 < k; --i) {
            // In case there is no solution
            if (i == 0 && k > 0)
                return new HashSet<>();

            int wi = weights[i - 1];

            if (wi <= w) {
                double actual = cache[k - 1][i - 1][w - wi];
                double expected = cache[k][i][w] - values[i - 1];

                //System.out.println("[" + (k-1) + ", " + (i-1) + ", " + (w-wi) + "]");

                if (actual == expected) {
                    w -= wi;
                    --k;
                    result.add(items[i - 1]);
                }
            }
        }

        return result;
    }
}
