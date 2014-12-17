package imdb;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImdbParser {

    public enum Table {
        ACTORS(1),                  // 00001
        DIRECTORS(2),               // 00010
        MOVIES(4),                  // 00100

        DIRECTOR_GENRES(8),         // 01000
        MOVIE_GENRES(16),           // 10000

        ALL(~0);

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

    public static void Parse(String filename) {
        Parse(filename, Table.getLoad(Table.ALL));
    }

    public static void Parse(String filename, int load) {
        try {
            BufferedReader r = new BufferedReader(new FileReader(filename));
            String line = r.readLine();
            int mode = 0;
            while (line != null) {
                if (line.startsWith("LOCK TABLES") && line.endsWith("WRITE;")) {
                    String table = line.substring(13, line.lastIndexOf("WRITE;") - 2);
                    System.out.println("Parsing: " + table.replace('_', ' '));
                } else {
                    String[] tokens = ParseLine(line);
                    switch (mode) {
                        case 1:     //actors
                            if (tokens.length != 5)
                                throw new IOException("Actor of length " + tokens.length + ": " + line);

                            if (Table.shouldLoad(load, Table.ACTORS))
                                new Actor(Integer.parseInt(tokens[0]), tokens[1], tokens[2], ((tokens[3].equals("M")) ? Gender.Male : Gender.Female), Integer.parseInt(tokens[4]));

                            break;
                        case 2:     //directors
                            if (tokens.length != 3)
                                throw new IOException("Director of length " + tokens.length + ": " + line);

                            if (Table.shouldLoad(load, Table.DIRECTORS))
                                new Director(Integer.parseInt(tokens[0]), tokens[1], tokens[2]);

                            break;
                        case 3:     //directors_genres
                            if (tokens.length != 3)
                                throw new IOException("Directors_genres of length " + tokens.length + ": " + line);

                            if (Table.shouldLoad(load, Table.DIRECTORS, Table.DIRECTOR_GENRES))
                            {
                                Director dir = Director.get(Integer.parseInt(tokens[0]));
                                if (dir != null)
                                    dir.addGenre(tokens[1]); //TODO: last float is ignored
                            }
                            break;
                        case 4:     //movies
                            if (tokens.length != 5)
                                throw new IOException("Movie of length " + tokens.length + ": " + line);

                            if (Table.shouldLoad(load, Table.MOVIES))
                                new Movie(Integer.parseInt(tokens[0]), tokens[1], Integer.parseInt(tokens[2]), ((tokens[3].equals("NULL")) ? -1 : Float.parseFloat(tokens[3])), Integer.parseInt(tokens[4]));

                            break;
                        case 5:     //movies_directors
                            if (tokens.length != 2)
                                throw new IOException("Movies_directors of length " + tokens.length + ": " + line);

                            if (Table.shouldLoad(load, Table.DIRECTORS, Table.MOVIES)) {
                                Director dir2 = Director.get(Integer.parseInt(tokens[1]));
                                Movie mov2 = Movie.get(Integer.parseInt(tokens[0]));

                                if (dir2 != null && mov2 != null)
                                    mov2.addDirector(dir2);
                            }
                            break;
                        case 6:     //movies_genres
                            if (tokens.length != 2)
                                throw new IOException("Movies_genres of length " + tokens.length + ": " + line);

                            if (Table.shouldLoad(load, Table.MOVIE_GENRES)) {
                                Movie mov3 = Movie.get(Integer.parseInt(tokens[0]));
                                if (mov3 != null)
                                    mov3.addGenre(tokens[1]);
                            }
                            break;
                        case 7:     //roles
                            if (tokens.length != 3)
                                throw new IOException("Role of length " + tokens.length + ": " + line);

                            if (Table.shouldLoad(load, Table.MOVIES, Table.ACTORS)) {
                                Actor act4 = Actor.get(Integer.parseInt(tokens[0]));
                                Movie mov4 = Movie.get(Integer.parseInt(tokens[1]));
                                if (act4 != null && mov4 != null)
                                    Role.addRole(mov4, act4, tokens[2]);
                            }
                            break;
                    }
                }
                line = r.readLine();
            }
            System.out.println("Done parsing data.");
        } catch (FileNotFoundException ex) {
            System.out.println("We got a problem: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("We got a problem: " + ex.getMessage());
        }
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
        return result.toArray(new String[result.size()]);
    }

    private static void OutputMovies(String outputFolder) {
        File o = new File(outputFolder);
        deleteDir(o);
        o.mkdir();

        int moviesPerFile = 10000;

        Iterator<Movie> mIt = Movie.getAll().iterator();
        int numFiles = 0;
        while (mIt.hasNext()) {
            String filename = numFiles++ + "";
            try {
                PrintWriter w = new PrintWriter(outputFolder + "/" + filename, "UTF-8");
                int i = 0;
                while (mIt.hasNext() && i++ < moviesPerFile) {
                    Movie m = mIt.next();
                    if (m.getRating() < 0 || m.getRoles().size() < 1) { --i; continue;}
                    w.print(m.getId()+",");
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
        }
        System.out.println("Done outputting files.");
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
        Parse("data/imdb-r.txt");
        OutputMovies("data/ActorRating/");
    }
}