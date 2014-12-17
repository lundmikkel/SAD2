package HnAActorRating;

import engine.Collector;
import engine.Mapper;
import engine.Tuple;

public class HubsAuthActorRatingRound1Mapper implements Mapper<
        ActorMovie,
        Iterable<ActorMovie>,

        Character,
        Tuple<ActorMovie, Iterable<ActorMovie>>> {

    @Override
    public void map(ActorMovie am, Iterable<ActorMovie> neighbours, Collector<Character, Tuple<ActorMovie, Iterable<ActorMovie>>> collector) {
        float weight = 0f;
        for (Tuple<String, Float> n : neighbours){
            weight += n.value;
        }
        char type = am.key.charAt(0);
        am = new ActorMovie(am.key, weight); //Since it is immutable, this is necessary
        collector.collect(type, new Tuple<>(am, neighbours));
    }
}
