package imdb;

import knapsack.Knapsack;
import knapsack.KnapsackHelper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author marcher89
 */
public class ImdbParser {

    public static void Parse(String filename) {
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
                            new Actor(Integer.parseInt(tokens[0]), tokens[1], tokens[2], ((tokens[3].equals("M")) ? Gender.Male : Gender.Female), Integer.parseInt(tokens[4]));
                            break;
                        case 2:     //directors
                            if (tokens.length != 3) {
                                throw new IOException("Director of length " + tokens.length + ": " + line);
                            }
                            new Director(Integer.parseInt(tokens[0]), tokens[1], tokens[2]);
                            break;
                        case 3:     //directors_genres
                            if (tokens.length != 3) {
                                throw new IOException("Directors_genres of length " + tokens.length + ": " + line);
                            }
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
                            new Movie(Integer.parseInt(tokens[0]), tokens[1], Integer.parseInt(tokens[2]), ((tokens[3].equals("NULL")) ? -1 : Float.parseFloat(tokens[3])), Integer.parseInt(tokens[4]));
                            break;
                        case 5:     //movies_directors
                            if (tokens.length != 2) {
                                throw new IOException("Movies_directors of length " + tokens.length + ": " + line);
                            }
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
        System.out.println("Directors: Found: " + df + " -  Not found: " + dnf);
        System.out.println("Actors: Found: " + af + " -  Not found: " + anf);
        System.out.println("Movies: Found: " + mf + " -  Not found: " + mnf);

    }

    public static void main(String[] args) {
        Parse("dataset/imdb-r.txt");
        List<Movie> movies = Movie.getAll().stream()
                .filter(m -> m.getRating() >= 9)
                .collect(Collectors.toList());

        Knapsack.knapsack(movies, 60_000, 10, new KnapsackHelper<Movie>() {
            @Override
            public int getWeight(Movie movie) {
                return movie.getDuration();
            }

            @Override
            public double getValue(Movie movie) {
                return movie.getRating();
            }
        }).forEach(System.out::println);

        final AtomicInteger W = new AtomicInteger(60_000);
        Set<Movie> solution = new HashSet<>();
        movies.sort((m1, m2) -> m2.getDuration() - m1.getDuration());
        movies.forEach(m -> {
            if (m.getDuration() <= W.get()) {
                solution.add(m);
                W.addAndGet(-m.getDuration());
            }
        });
        movies.forEach(System.out::println);
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
}
