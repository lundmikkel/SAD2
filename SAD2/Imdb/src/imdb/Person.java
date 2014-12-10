package imdb;

public abstract class Person {
    // region Fields

    private final int id;
    private final String firstName;
    private final String lastName;

    // endregion

    // region Constructor

    public Person(int id, String firstName, String lastName) {
        if (id < 0)
            throw new IllegalArgumentException("id must be non-negative");
        if (firstName == null)
            throw new IllegalArgumentException("firstName cannot be null");
        if (lastName == null)
            throw new IllegalArgumentException("lastName cannot be null");

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // endregion

    // region Getters

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // endregion

    // region Overrides

    @Override
    public int hashCode() {
        return id * 59;
    }

    @Override
    public boolean equals(Object obj) {
        // Not strictly necessary, but often a good optimization
        if (this == obj)
            return true;
        if (!(obj instanceof Person) || !this.getClass().equals(obj.getClass()))
            return false;
        Person that = (Person) obj;
        return this.getId() == that.getId();
    }

    @Override
    public String toString() {
        return getFirstName()+" "+getLastName()+" ("+getId()+")";
    }

    // endregion
}
