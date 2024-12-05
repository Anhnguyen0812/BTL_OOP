package library.model;

/**
 * ConcreteUser class extends User to provide specific user implementations.
 */
public class ConcreteUser extends User {
    /**
     * Constructor to initialize ConcreteUser with id, name, and email.
     * 
     * @param id    the user id
     * @param name  the user name
     * @param email the user email
     */
    public ConcreteUser(int id, String name, String email) {
        super(id, name, email);
    }

    /**
     * Constructor to initialize ConcreteUser with name, email, password, role, and
     * salt.
     * 
     * @param name     the user name
     * @param email    the user email
     * @param password the user password
     * @param role     the user role
     * @param salt     the salt for password hashing
     */
    public ConcreteUser(String name, String email, String password, String role, String salt) {
        super(name, email, password, role, salt);
    }

    /**
     * Constructor to initialize ConcreteUser with id, name, email, password, role,
     * and salt.
     * 
     * @param id       the user id
     * @param name     the user name
     * @param email    the user email
     * @param password the user password
     * @param role     the user role
     * @param salt     the salt for password hashing
     */
    public ConcreteUser(int id, String name, String email, String password, String role, String salt) {
        super(id, name, email, password, role, salt);
    }

    /**
     * Gets the user role.
     * 
     * @return an empty string as the role
     */
    @Override
    public String getRole() {
        return "";
    }

}
