package imdb;

import knapsack.Knapsack;
import knapsack.KnapsackHelper;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author marcher89
 */
public class ImdbParser {

    private enum Table {
        ACTORS(1),                  // 00001
        DIRECTORS(2),               // 00010
        MOVIES(4),                  // 00100

        DIRECTOR_GENRES(8),         // 01000
        MOVIE_GENRES(16),           // 10000

        ALL(~1);

        private final int value;

        private Table(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static boolean shouldLoad(int load, Table... tables) {
            int value = 0;
            for (Table table : tables)
                value |= table.getValue();

            return (load & value) > 0;
        }

        public static int getLoad(Table... tables) {
            int value = 0;
            for (Table table : tables)
                value |= table.getValue();
            return value;
        }
    }

    public static void Parse(String filename, int load) {
        int dnf = 0, mnf = 0, anf = 0,
                df = 0, mf = 0, af = 0;
        try {
            BufferedReader r = new BufferedReader(new FileReader(filename));
            String line = r.readLine();
            int mode = 0;
            while (line != null) {
                if (line.startsWith("LOCK TABLES") && line.endsWith("WRITE;")) {
                    String table = line.substring(13, line.lastIndexOf("WRITE;") - 2);
                    System.out.println("Table: " + table + " mode: " + (++mode));
                } else {
                    String[] tokens = ParseLine(line);
                    switch (mode) {
                        case 1:     //actors
                            if (tokens.length != 5) {
                                throw new IOException("Actor of length " + tokens.length + ": " + line);
                            }

                            if (!Table.shouldLoad(load, Table.ACTORS))
                                break;

                            new Actor(Integer.parseInt(tokens[0]), tokens[1], tokens[2], ((tokens[3].equals("M")) ? Gender.Male : Gender.Female), Integer.parseInt(tokens[4]));
                            break;
                        case 2:     //directors
                            if (tokens.length != 3) {
                                throw new IOException("Director of length " + tokens.length + ": " + line);
                            }

                            if (!Table.shouldLoad(load, Table.DIRECTORS))
                                break;

                            new Director(Integer.parseInt(tokens[0]), tokens[1], tokens[2]);
                            break;
                        case 3:     //directors_genres
                            if (tokens.length != 3) {
                                throw new IOException("Directors_genres of length " + tokens.length + ": " + line);
                            }

                            if (!Table.shouldLoad(load, Table.DIRECTORS, Table.DIRECTOR_GENRES))
                                break;

                            Director dir = Director.get(Integer.parseInt(tokens[0]));
                            if (dir == null) {
                                //System.out.println("Did not find director with id: " + tokens[0]);
                                dnf++;
                            } else {
                                df++;
                                dir.addGenre(tokens[1]); //TODO: last float is ignored
                            }
                            break;
                        case 4:     //movies
                            if (tokens.length != 5) {
                                throw new IOException("Movie of length " + tokens.length + ": " + line);
                            }

                            if (!Table.shouldLoad(load, Table.MOVIES))
                                break;

                            new Movie(Integer.parseInt(tokens[0]), tokens[1], Integer.parseInt(tokens[2]), ((tokens[3].equals("NULL")) ? -1 : Float.parseFloat(tokens[3])), Integer.parseInt(tokens[4]));
                            break;
                        case 5:     //movies_directors
                            if (tokens.length != 2) {
                                throw new IOException("Movies_directors of length " + tokens.length + ": " + line);
                            }

                            if (!Table.shouldLoad(load, Table.DIRECTORS, Table.MOVIES))
                                break;

                            Director dir2 = Director.get(Integer.parseInt(tokens[1]));
                            Movie mov2 = Movie.get(Integer.parseInt(tokens[0]));
                            if (dir2 == null) {
                                //System.out.println("Did not find director with id: " + tokens[1]);
                                dnf++;
                            } else if (mov2 == null) {
                                //System.out.println("Did not find movie with id: " + tokens[0]);
                            } else {
                                df++;
                                mf++;
                                mov2.addDirector(dir2);
                            }
                            break;
                        case 6:     //movies_genres
                            if (tokens.length != 2) {
                                throw new IOException("Movies_genres of length " + tokens.length + ": " + line);
                            }

                            if (!Table.shouldLoad(load, Table.MOVIE_GENRES))
                                break;

                            Movie mov3 = Movie.get(Integer.parseInt(tokens[0]));
                            if (mov3 == null) {
                                //System.out.println("Did not find movie with id: " + tokens[0]);
                                mnf++;
                            } else {
                                mf++;
                                mov3.addGenre(tokens[1]);
                            }
                            break;
                        case 7:     //roles
                            if (tokens.length != 3) {
                                throw new IOException("Role of length " + tokens.length + ": " + line);
                            }

                            if (!Table.shouldLoad(load, Table.MOVIES, Table.ACTORS))
                                break;

                            Actor act4 = Actor.get(Integer.parseInt(tokens[0]));
                            Movie mov4 = Movie.get(Integer.parseInt(tokens[1]));
                            if (act4 == null) {
                                //System.out.println("Did not find actor with id: " + tokens[0]);
                                anf++;
                            } else if (mov4 == null) {
                                //System.out.println("Did not find movie with id: " + tokens[1]);
                                mnf++;
                            } else {
                                af++;
                                mf++;
                                Role.addRole(mov4, act4, tokens[2]);
                            }
                            break;
                    }
                }
                line = r.readLine();
            }
        } catch (FileNotFoundException ex) {
            //TODO: to do what?!
            System.out.println("We got a problem: " + ex.getMessage());
        } catch (IOException ex) {
            //TODO: to do what?!
            System.out.println("We got a problem: " + ex.getMessage());
        }
        //System.out.println("Directors: Found: " + df + " -  Not found: " + dnf);
        //System.out.println("Actors: Found: " + af + " -  Not found: " + anf);
        //System.out.println("Movies: Found: " + mf + " -  Not found: " + mnf);
    }

    private static String[] ParseLine(String line) {
        List<String> result = new ArrayList<>();
        int currPos = 1; //There is a space in the start of the line(!)
        while (true) {
            StringBuilder sb = new StringBuilder();
            boolean quoted = false;
            if (line.charAt(currPos) == '\'') { //String is quoted!
                quoted = true;
                currPos++;
            }
            while (true) {
                sb.append(line.charAt(currPos));
                if (line.charAt(currPos) == '\'') {
                    quoted = !quoted;
                    sb.deleteCharAt(sb.length() - 1);
                } else if (currPos + 2 < line.length()
                        && line.substring(currPos, currPos + 2).equals("\\'") && quoted) {
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append('\'');
                    currPos++;
                    //System.out.println("skipping ");
                } else if (line.charAt(currPos) == ',' && !quoted) {
                    currPos++;
                    sb.deleteCharAt(sb.length() - 1);
                    break;
                }

                currPos++;
                if (currPos >= line.length()) {
                    break;
                }
            }
            result.add(sb.toString());
            if (currPos >= line.length()) {
                break;
            }
        }
        /*for (String string : result) {
         System.out.print(string+"|");
         }
         System.out.println();*/
        return result.toArray(new String[result.size()]);
    }

    private static void OutputMovies(String outputFolder) {
        File o = new File(outputFolder);
        deleteDir(o);
        o.mkdir();

        int moviesPerFile = 100;
        int tempbreak = 10;

        Iterator<Movie> mIt = Movie.getAll().iterator();
        int numFiles = 0;
        while (mIt.hasNext()) {
            String filename = numFiles++ + "";
            try {
                PrintWriter w = new PrintWriter(outputFolder + "/" + filename, "UTF-8");
                int i = 0;
                while (mIt.hasNext() && i++ < moviesPerFile) {
                    Movie m = mIt.next();
                    if (m.getRating() < 0 || m.getRoles().size() < 1) {
                        --i;
                        continue;
                    }
                    w.print(m.getRating());
                    for (Role r : m.getRoles()) {
                        w.print("," + r.getActor().getId());
                    }
                    w.println();
                }
                w.close();
            } catch (IOException e) {
                System.err.println("Unable to write to file: '" + filename + "': ");
                e.printStackTrace();
            }
            //if (--tempbreak < 1) break;
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static void main(String[] args) {
        int load = Table.getLoad(Table.ACTORS, Table.MOVIES);
        Parse("data/imdb-r.txt", load);
        //List<Movie> movies = Movie.getAll().stream()
        //        .filter(m -> m.getRating() >= 9)
        //        .collect(Collectors.toList());

        //Actor.getAll().stream().sorted((a,b)-> a.getMovieCount() - b.getMovieCount()).forEach(a -> System.out.println(a + " " + a.getMovieCount()));

        //if (true)
        //    return;

        /*
        System.out.println("Movies:\t"+Movie.count());
        System.out.println("Actors:\t"+Actor.count());
        System.out.println("Directors:\t"+Director.getAll().size());
        System.out.println("Max Movie id:\t"+Movie.getAll().stream().max((x1, x2) -> (int)Math.signum(x1.getId() - x2.getId())).get().getId());
        System.out.println("Max Actor id:\t"+Actor.getAll().stream().max((x1, x2) -> (int) Math.signum(x1.getId() - x2.getId())).get().getId());
        System.out.println("Max Director id:\t"+Director.getAll().stream().max((x1, x2) -> (int) Math.signum(x1.getId() - x2.getId())).get().getId());
        System.out.println("Movies with rating and actors:\t"+Movie.getAll().stream().filter(m -> m.getRating()>0 && m.getRoles().size() > 0).count());
        OutputMovies("data/ActorRating/");

        System.out.println("DONE");


        if (true) return;
        */


        /*
        actors.sort((x, y) -> x.getName().length() - y.getName().length());

        int max = actors.get(actors.size() - 1).getName().length();

        int[] histogram = new int[max + 1];

        for (Actor actor : actors) {
            int length = actor.getName().length();
            histogram[length]++;
        }

        for (int i = 1; i <= max; ++i) {
            System.out.printf("%3d -> %7d\n", i, histogram[i]);
        }

        if (true) return;
        //*/

        int K = 14;
        int W = 105;
        double sf = 1.0d;

        KnapsackHelper<Actor> knapsackHelper = new KnapsackHelper<Actor>() {
            @Override
            public int getWeight(Actor actor) {
                return actor.getName().length();
            }

            @Override
            public double getValue(Actor actor) {
                // TODO: Update to proper rating
                return actor.getMovieCount();
            }
        };


        List<Actor> actors = Actor.getAll().stream()
                .limit(2000)
                        //.sorted((x, y) -> { int compare = knapsackHelper.getWeight(x) - knapsackHelper.getWeight(y); return compare != 0 ? compare : Double.compare(knapsackHelper.getValue(y), knapsackHelper.getValue(x));})
                .collect(Collectors.toList());


        long start = System.currentTimeMillis();
        int weight = 0;
        int value = 0;
        List<Actor> cast = new ArrayList<>(K);

        /*for (int k = 0; k < K; ++k) {
            Actor actor = actors.get(k);

            cast.add(actor);
            weight += knapsackHelper.getWeight(actor);
            value += knapsackHelper.getValue(actor);
        }
        if (weight > W)
            cast.clear();

        System.out.println("\nWeight: " + weight + ", value: " + value);
        cast.forEach(System.out::println);*/


        start = System.currentTimeMillis();
        cast = Knapsack.knapsack(actors, K, W, sf, knapsackHelper, false);
        System.out.println("\nTime: " + (System.currentTimeMillis() - start));

        cast.sort((x, y) -> knapsackHelper.getWeight(x) - knapsackHelper.getWeight(y));

        weight = 0;
        value = 0;
        for (Actor actor : cast) {
            weight += knapsackHelper.getWeight(actor);
            value += knapsackHelper.getValue(actor);
        }

        System.out.println("Weight: " + weight + ", value: " + value);
        cast.forEach(System.out::println);


        start = System.currentTimeMillis();
        cast = Knapsack.knapsack(actors, K, W, sf, knapsackHelper, true);
        System.out.println("\nTime: " + (System.currentTimeMillis() - start));

        cast.sort((x, y) -> knapsackHelper.getWeight(x) - knapsackHelper.getWeight(y));

        weight = 0;
        value = 0;
        for (Actor actor : cast) {
            weight += knapsackHelper.getWeight(actor);
            value += knapsackHelper.getValue(actor);
        }

        System.out.println("Weight: " + weight + ", value: " + value);
        cast.forEach(System.out::println);

        System.out.println("Done");


        //final AtomicInteger W2 = new AtomicInteger(60_000);
        //Set<Movie> solution = new HashSet<>();
        //movies.sort((m1, m2) -> m2.getDuration() - m1.getDuration());
        //movies.forEach(m -> {
        //    if (m.getDuration() <= W2.get()) {
        //        solution.add(m);
        //        W2.addAndGet(-m.getDuration());
        //    }
        //});
        //movies.forEach(System.out::println);
    }
}
