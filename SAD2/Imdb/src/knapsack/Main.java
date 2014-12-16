package knapsack;

import imdb.Actor;
import imdb.ImdbParser;
import imdb.Movie;
import sun.rmi.server.InactiveGroupException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lundmikkel on 16/12/14.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        int load = ImdbParser.Table.getLoad(ImdbParser.Table.ACTORS, ImdbParser.Table.MOVIES);
        ImdbParser.Parse("data/imdb-r.txt", load);


        List<Actor> actors = new ArrayList<>();

        System.out.println("Reading ratings from file.");
        try(BufferedReader br = new BufferedReader(new FileReader("data/movie_cast.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t");
                Actor actor = Actor.get(Integer.parseInt(tokens[0]));
                double rating = Double.parseDouble(tokens[2]);
                actor.setCustomRating(rating);
                actors.add(actor);
            }
        }

        int K = 14;
        int W = 160;
        double sf = 1d;


        List<Actor> cast = null;
        KnapsackHelper<Actor> knapsackHelper = new KnapsackHelper<Actor>() {
            @Override
            public int getWeight(Actor actor) {
                return actor.getName().length();
            }
            @Override
            public double getValue(Actor actor) {
                return actor.getCustomRating();
            }
        };

        System.out.println("Calculating best cast.");
        for (W = 160; W >= 150; --W) {
            System.out.println("W: " + W);
            cast = Knapsack.knapsack(actors, K, W, sf, knapsackHelper, true);
            cast.sort((x, y) -> knapsackHelper.getWeight(x) - knapsackHelper.getWeight(y));
        }

        int weight = 0;
        double profit = 0;
        for (Actor actor : cast) {
            weight += knapsackHelper.getWeight(actor);
            profit += knapsackHelper.getValue(actor);
        }

        System.out.printf("\nTotal weight: %d, total profit: %f\n\n", weight, profit);

        System.out.println();
        cast.forEach(System.out::println);
    }
}
