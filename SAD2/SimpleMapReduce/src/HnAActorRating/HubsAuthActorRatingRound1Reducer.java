package HnAActorRating;

import engine.Collector;
import engine.Reducer;
import engine.Tuple;

/**
 * Created by marcher89 on 15/12/14.
 */
public class HubsAuthActorRatingRound1Reducer implements Reducer<
        Character,
        Tuple<ActorMovie, Iterable<ActorMovie>>,
        ActorMovie,
        Iterable<ActorMovie>> {

    public static volatile float actorDividend = 1f,
                                 movieDividend = 1f;

    @Override
    public void reduce(Character type, Iterable<Tuple<ActorMovie, Iterable<ActorMovie>>> elements, Collector<ActorMovie, Iterable<ActorMovie>> collector) {
        float sum = 0f;
        for (Tuple<ActorMovie, Iterable<ActorMovie>> element : elements){
            ActorMovie am = element.key;
            Iterable<ActorMovie> neighbours = element.value;

            sum += am.value * am.value;
            collector.collect(am, neighbours);
        }
        float div = (float) Math.sqrt(sum);
        if(type == 'A') actorDividend = div;
        else if(type == 'M') movieDividend = div;
        else throw new IllegalArgumentException("Type character was '"+type+"', expected 'A' or 'M'.");
    }
}
