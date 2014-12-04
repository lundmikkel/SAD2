package engine;

/**
 * Created by Rasmus on 04-12-2014.
 */
public interface Mapper<InKey, InValue, OutKey, OutValue> {
    public void map(InKey key, InValue value, Collector<OutKey, OutValue> collector) throws Exception;
}
