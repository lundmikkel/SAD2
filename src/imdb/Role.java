package imdb;

public class Role {
    // region Fields

    private final Movie movie;
    private final Actor actor;
    private final String description;

    // endregion

    // region Constructor

    private Role(Movie movie, Actor actor, String description) {
        if (movie == null)
            throw new IllegalArgumentException("movie cannot be null");
        if (actor == null)
            throw new IllegalArgumentException("actor cannot be null");
        if (description == null)
            throw new IllegalArgumentException("description cannot be null");
        
        this.movie = movie;
        this.actor = actor;
        this.description = description;
    }

    // endregion

    //region Getters

    public Movie getMovie() {
        return movie;
    }

    public Actor getActor() {
        return actor;
    }

    public String getDescription() {
        return description;
    }

    //endregion

    // region Adders

    public static boolean addRole(Movie movie, Actor actor, String description) {
        Role role = new Role(movie, actor, description);

        return movie.addRole(role) && actor.addRole(role);
    }

    // endregion

    // region Overrides

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 31 + movie.hashCode();
        hash = hash * 31 + actor.hashCode();
        hash = hash * 31 + description.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        // Not strictly necessary, but often a good optimization
        if (this == obj)
            return true;
        if (!(obj instanceof Role))
            return false;
        Role that = (Role) obj;
        return this.movie.equals(that.movie) &&
               this.actor.equals(that.actor) &&
               this.description.equals(that.description);
    }

    // endregion
}
