package HnAActorRating;

import engine.*;

public class HubsAuthActorRatingRound2Mapper implements Mapper<
        ActorMovie,
        Iterable<ActorMovie>,

        ActorMovie,
        ActorMovie> {
    @Override
    public void map(ActorMovie am, Iterable<ActorMovie> neighbours, Collector<ActorMovie, ActorMovie> collector) {
        char type = am.key.charAt(0);
        float div = (type == 'A') ? HubsAuthActorRatingRound1Reducer.actorDividend
                                  : HubsAuthActorRatingRound1Reducer.movieDividend;
        float newweight = am.value / div;

        //Since ActorMovie is immutable, this is necessary
        am = new ActorMovie(am.key, newweight);

        for (ActorMovie n : neighbours) {
            collector.collect(n, am);
        }
    }
}
