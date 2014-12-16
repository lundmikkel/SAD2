package imdb;

import java.util.*;

public class Actor extends Person {
    private final static Map<Integer, Actor> instances = new HashMap<>();

    // region Fields

    private final Gender gender;
    private final int movieCount;
    private final Set<Role> roles;

    public double getCustomRating() {
        return customRating;
    }

    public void setCustomRating(double customRating) {
        this.customRating = customRating;
    }

    private double customRating;

    // endregion

    // region Constructor

    public Actor(int id, String firstName, String lastName, Gender gender, int movieCount) {
        super(id, firstName, lastName);
        if (id < 0)
            throw new IllegalArgumentException("id must be non-negative");
        if (movieCount < 0)
            throw new IllegalArgumentException("movieCount must be non-negative");


        // Set private fields
        this.gender = gender;
        this.movieCount = movieCount;

        // The set size should equal the movie count
        roles = new HashSet<>(movieCount);

        instances.put(id, this);
    }

    // endregion

    // region Getters

    public Gender getGender() {
        return gender;
    }

    public int getMovieCount() {
        return movieCount;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getFirstMovieTitle() {
        for (Role role : roles)
            return String.format(Locale.US, "%s (%.1f)", role.getMovie().getTitle(), role.getMovie().getRating());
        return "";
    }

    // endregion

    //region Adders

    public boolean addRole(Role role) {
        if (role == null)
            throw new IllegalArgumentException("role cannot be null");
        return roles.add(role);
    }

    //endregion

    // region Static methods

    public static Collection<Actor> getAll() {
        return instances.values();
    }

    public static Actor get(int id) {
        return instances.get(id);
    }

    public static int count() {
        return instances.size();
    }

    // endregion


    @Override
    public String toString() {
        return getName();
    }
}
