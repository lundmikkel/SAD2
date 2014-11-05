package knapsack;

/**
 * Created by Rasmus on 05-11-2014.
 */
public interface Knapsacker<T> {

    public int getWeight(T t);
    public double getValue(T t);


}
