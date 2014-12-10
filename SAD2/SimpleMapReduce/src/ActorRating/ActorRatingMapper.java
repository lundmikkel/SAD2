package ActorRating;

import engine.Collector;
import engine.Mapper;
import engine.Triple;

public class ActorRatingMapper implements Mapper<Integer, Iterable<Triple<Float, Integer, Float>>, Integer, Triple<Float, Integer, Float>> {
    final float alpha;
    final int actorCount;

    public ActorRatingMapper(float alpha, int actorCount) {
        this.alpha = alpha;
        this.actorCount = actorCount;
    }

    @Override
    public void map(Integer actorA, Iterable<Triple<Float, Integer, Float>> connections, Collector<Integer, Triple<Float, Integer, Float>> collector) {
        float sum = 0.0f;
        int count = 0;
        for (Triple<Float, Integer, Float> connection : connections) {
            sum += connection.item1 * connection.item3;
            count++;
        }
        Float actorRatingA = alpha / actorCount + (1 - alpha) * (sum / count);
        for (Triple<Float, Integer, Float> connection : connections) {
            collector.collect(connection.item2, connection.update(connection.item1, actorA, actorRatingA));
        }
    }
}
