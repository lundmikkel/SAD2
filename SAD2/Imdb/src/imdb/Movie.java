package imdb;

import java.util.*;

public class Movie {
    private final static Map<Integer, Movie> instances = new HashMap<>();

    // region Fields

    private final int id;
    private final String title;
    private final int year;
    // Negative values mean undefined
    private final double rating;
    private final int duration;
    private final Set<Director> directors = new HashSet<>();
    private final Set<String> genres = new HashSet<>();
    private final Set<Role> roles = new HashSet<>();

    // endregion

    // region Constructor

    public Movie(int id, String title, int year, float rating, int duration) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.rating = rating;
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

    public double getRating() {
        return rating;
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
        boolean result = directors.add(director);

        if (result)
            director.addMovie(this);

        return result;
    }

    public boolean addGenre(String genre) {
        return genres.add(genre);
    }

    public boolean addRole(Role role) {
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

    public static int count() {
        return instances.size();
    }

    // endregion

    // region Overrides

    @Override
    public String toString() {
        return String.format(Locale.US, "%s (%d) - %d mins [%.2f]", title, year, duration, rating);
    }

    @Override
    public int hashCode() {
        return id;
    }

    // endregion
}
