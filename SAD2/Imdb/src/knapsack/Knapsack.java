package knapsack;

import java.util.*;

public class Knapsack {
    // region Main

    public static void main(String[] args) {
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

        List<Item> test = new ArrayList<>(3);
        test.add(new Item(" 3", 3, 3));
        test.add(new Item(" 2", 1, 1));
        test.add(new Item(" 1", 1, 1));

        System.out.println("Iterative version:");
        knapsack(test, 1, 3, 1.0d, kh, true).forEach(i -> System.out.println("- " + i));
        System.out.println("Recursive version:");
        knapsack(test, 1, 3, 1.0d, kh, false).forEach(i -> System.out.println("- " + i));
        System.out.println();

        System.out.println("Iterative version:");
        knapsack(test, 2, 3, 1.0d, kh, true).forEach(i -> System.out.println("- " + i));
        System.out.println("Recursive version:");
        knapsack(test, 2, 3, 1.0d, kh, false).forEach(i -> System.out.println("- " + i));
        System.out.println();

        System.out.println("Iterative version:");
        knapsack(test, 3, 3, 1.0d, kh, true).forEach(i -> System.out.println("- " + i));
        System.out.println("Recursive version:");
        knapsack(test, 3, 3, 1.0d, kh, false).forEach(i -> System.out.println("- " + i));
        System.out.println();
    }

    // endregion Main

    // region Cache

    private static interface Cache {
        boolean contains(int x, int y, int z);

        double set(int x, int y, int z, double value);

        Double get(int x, int y, int z);
    }

    private static class ArrayCache implements Cache {
        Double[][][] cache;

        public ArrayCache(int X, int Y, int Z) {
            cache = new Double[X + 1][Y + 1][Z + 1];
        }

        @Override
        public boolean contains(int x, int y, int z) {
            return cache[x][y][z] != null;
        }

        @Override
        public double set(int x, int y, int z, double value) {
            return cache[x][y][z] = value;
        }

        @Override
        public Double get(int x, int y, int z) {
            return cache[x][y][z];
        }
    }

    private static class HashCache implements Cache {
        HashMap<Key, Double> cache = new HashMap<>();

        public boolean contains(int x, int y, int z) {
            return cache.containsKey(new Key(x, y, z));
        }

        public boolean contains(Key key) {
            return cache.containsKey(key);
        }

        public double set(int x, int y, int z, double value) {
            cache.put(new Key(x, y, z), value);
            return value;
        }

        public void set(Key key, double value) {
            cache.put(key, value);
        }

        public Double get(int x, int y, int z) {
            return cache.get(new Key(x, y, z));
        }
    }

    private static class Key {
        public final int x, y, z;
        final int hash;

        public Key(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;

            hash = computeHash();
        }

        private int computeHash() {
            int hash = 31;
            hash = hash * 23 + x;
            hash = hash * 41 + y;
            hash = hash * 29 + z;
            return hash;
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null)
                return false;

            Key that = (Key) o;
            return this.hash == that.hash &&
                    this.x == that.x &&
                    this.y == that.y &&
                    this.z == that.z;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ", " + z + ")";
        }
    }

    // endregion Cache

    public static <T> List<T> knapsack(List<T> itemsList, int K, int W, double scalingFactor, KnapsackHelper<T> knapsackHelper, boolean iterative) {
        T[] items = (T[]) itemsList.toArray();
        int N = items.length;

        int[] weights = new int[N];
        double[] values = new double[N];

        // Scaling
        W = (int) Math.floor(W / scalingFactor);
        for (int i = 0; i < N; ++i) {
            weights[i] = (int) Math.ceil(weights[i] / scalingFactor);
        }

        // Caching
        for (int i = 0; i < N; ++i) {
            weights[i] = knapsackHelper.getWeight(items[i]);
            values[i] = knapsackHelper.getValue(items[i]);
        }

        // Create cache
        Cache cache = new ArrayCache(K, N, W);

        // Find optimal solution
        if (iterative)
            findOptimalIteratively(K, N, W, weights, values, cache);
        else
            findOptimalRecursively(K, N, W, weights, values, cache);

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

        return backtrackSolution(K, N, W, items, weights, values, cache);
    }

    // region Find Optimal Solution

    private static void findOptimalIteratively(int K, int N, int W, int[] weights, double[] values, Cache cache) {
        for (int k = 0; k <= K; ++k) {

            // Iterate options
            for (int i = 0; i <= N; ++i) {

                // Iterate weight limits
                for (int w = 0; w <= W; ++w) {

                    // All positions have been filled
                    if (k == 0) {
                        cache.set(k, i, w, 0.0);
                        continue;
                    }

                    // Invalid solution
                    if (i < k) {
                        cache.set(k, i, w, Double.NEGATIVE_INFINITY);
                        continue;
                    }

                    // Too heavy, skip
                    int wi = weights[i - 1];
                    if (w < wi) {
                        cache.set(k, i, w, cache.get(k, i - 1, w));
                        continue;
                    }

                    // Get possible values
                    double selected = cache.get(k - 1, i - 1, w - wi) + values[i - 1];
                    double notSelected = cache.get(k, i - 1, w);

                    // Save max value
                    cache.set(k, i, w, Math.max(selected, notSelected));
                }
            }
        }
    }

    private static double findOptimalRecursively(int k, int i, int w, int weights[], double[] values, Cache cache) {
        // Returned cached value
        if (cache.contains(k, i, w))
            return cache.get(k, i, w);

        // All positions have been filled
        if (k == 0)
            return cache.set(k, i, w, 0.0);

        // Invalid solution
        if (i < k)
            return cache.set(k, i, w, Double.NEGATIVE_INFINITY);

        // Too heavy, skip
        int wi = weights[i - 1];
        if (w < wi)
            return cache.set(k, i, w, findOptimalRecursively(k, i - 1, w, weights, values, cache));

        // Get possible values
        double selected = findOptimalRecursively(k - 1, i - 1, w - wi, weights, values, cache) + values[i - 1];
        double notSelected = findOptimalRecursively(k, i - 1, w, weights, values, cache);

        // Cache and return max value
        return cache.set(k, i, w, Math.max(selected, notSelected));
    }

    private static <T> List<T> backtrackSolution(int K, int N, int W, T[] items, int[] weights, double[] values, Cache cache) {
        List<T> results = new ArrayList<>(K);

        System.out.println("Best solution : " + cache.get(K, N, W));

        // Check if there is a solution
        if (cache.get(K, N, W).isInfinite())
            return results;

        // Find the solution
        for (int i = N, w = W, k = K; k > 0 && i > 0; --i) {
            // Check if option fits current weight
            int wi = weights[i - 1];
            if (wi <= w) {
                // Get the actual and expected values
                Double actual = cache.get(k - 1, i - 1, w - wi);
                double expected = cache.get(k, i, w) - values[i - 1];

                if (actual != null && actual == expected) {
                    // Pick option and correct parameters
                    w -= wi;
                    k -= 1;
                    results.add(items[i - 1]);
                }
            }
        }

        return results;
    }

    // endregion Find Optimal Solution
}
