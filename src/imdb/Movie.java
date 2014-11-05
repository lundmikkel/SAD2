package imdb;

import java.util.*;

public class Movie {
    private final static Map<Integer, Movie> instances = new HashMap<>();

    // region Fields

    private final int id;
    private final String title;
    private final int year;
    // Negative values mean undefined
    private final float rank;
    private final int duration;
    private final Set<Director> directors = new HashSet<>();
    private final Set<String> genres = new HashSet<>();
    private final Set<Role> roles = new HashSet<>();

    // endregion

    // region Constructor

    public Movie(int id, String title, int year, float rank, int duration) {
        if (id < 0)
            throw new IllegalArgumentException("id must be non-negative");
        if (title == null)
            throw new IllegalArgumentException("title cannot be null");
        if (year < 0)
            throw new IllegalArgumentException("year must be non-negative");
        if (duration < 0)
            throw new IllegalArgumentException("duration must be non-negative");

        this.id = id;
        this.title = title;
        this.year = year;
        this.rank = rank;
        this.duration = duration;

        instances.put(id, this);
    }

    // endregion

    // region Getters

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public float getRank() {
        return rank;
    }

    public int getDuration() {
        return duration;
    }

    public Set<Director> getDirectors() {
        return Collections.unmodifiableSet(directors);
    }

    public Set<String> getGenres() {
        return Collections.unmodifiableSet(genres);
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    // endregion

    // region Adders

    public boolean addDirector(Director director) {
        if (director == null)
            throw new IllegalArgumentException("director cannot be null");
        
        boolean result = directors.add(director);

        if (result)
            director.addMovie(this);

        return result;
    }

    public boolean addGenre(String genre) {
        if (genre == null)
            throw new IllegalArgumentException("genre cannot be null");
        return genres.add(genre);
    }

    public boolean addRole(Role role) {
        if (role == null)
            throw new IllegalArgumentException("role cannot be null");
        return roles.add(role);
    }

    // endregion

    // region Static methods

    public static Collection<Movie> getAll() {
        return instances.values();
    }

    public static Movie get(int id) {
        return instances.get(id);
    }

    // endregion

    // region Overrides

    @Override
    public int hashCode() {
        return id;
    }

    // endregion
}
