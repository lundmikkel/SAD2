package imdb;

import java.util.*;

public class Director extends Person {
    private final static Map<Integer, Director> instances = new HashMap<>();

    // region Fields

    private final Set<String> genres = new HashSet<>();
    private final Set<Movie> movies = new HashSet<>();

    // endregion

    // region Constructor

    public Director(int id, String firstName, String lastName) {
        super(id, firstName, lastName);

        instances.put(id, this);
    }

    // endregion

    // region Getters

    public Set<String> getGenres() {
        return Collections.unmodifiableSet(genres);
    }

    public Set<Movie> getMovies() {
        return Collections.unmodifiableSet(movies);
    }

    // endregion

    //region Adders

    public boolean addGenre(String genre) {
        if (genre == null)
            throw new IllegalArgumentException("genre cannot be null");
        return genres.add(genre);
    }

    public boolean addMovie(Movie movie) {
        if (movie == null)
            throw new IllegalArgumentException("movie cannot be null");

        boolean result = movies.add(movie);

        if (result)
            movie.addDirector(this);

        return result;
    }

    //endregion

    // region Static methods

    public static Collection<Director> getAll() {
        return instances.values();
    }

    public static Director get(int id) {
        return instances.get(id);
    }

    // endregion
}