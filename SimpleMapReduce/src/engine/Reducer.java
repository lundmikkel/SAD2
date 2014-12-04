package engine;

import java.util.Iterator;

/**
 * Created by Rasmus on 04-12-2014.
 */
public interface Reducer<InKey, InValue, OutKey, OutValue> {
    public void reduce(InKey key, Iterable<InValue> values, Collector<OutKey, OutValue> collector) throws Exception;
}
