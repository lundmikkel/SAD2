package knapsack;

import imdb.Actor;
import imdb.ImdbParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        ImdbParser.Parse("data/imdb-r.txt", ImdbParser.Table.ACTORS, ImdbParser.Table.MOVIES);

        List<Actor> actors = new ArrayList<>();

        System.out.println("Reading ratings from file.");
        try(BufferedReader br = new BufferedReader(new FileReader("data/MovieCast.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t");
                Actor actor = Actor.get(Integer.parseInt(tokens[0]));
                double rating = Double.parseDouble(tokens[2]);
                actor.setCustomRating(rating);
                actors.add(actor);
            }
        }

        int K = 20;
        int W = 106;

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
        List<Actor> cast = Knapsack.knapsack(actors, K, W, knapsackHelper, true);
        cast.sort((x, y) -> knapsackHelper.getWeight(x) - knapsackHelper.getWeight(y));

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
