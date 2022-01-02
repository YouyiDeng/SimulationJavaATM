package atmMVC.model;

/**
 * An identifiable person.
 */
public class IdentifiablePerson {
    protected String username;
    protected String password;
    protected int sinNumber;

    /**
     * Constructs a new identifiable person.
     * @param username username of the person
     * @param password password of the person
     * @param sinNumber social insurance number of the person
     */
    public IdentifiablePerson(String username, String password, int sinNumber) {
        this.username = username;
        this.password = password;
        this.sinNumber = sinNumber;
    }

    public int getSinNumber() {
        return sinNumber;
    }

    public void setSinNumber(int value) {
        this.sinNumber = value;
    }
}
