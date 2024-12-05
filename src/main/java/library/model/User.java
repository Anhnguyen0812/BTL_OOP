package library.model;

/**
 * User class represents a user in the library system.
 */
public class User {
  protected int id = 0;
  protected String name;
  protected String email;
  protected String role;
  protected String password;
  protected String salt;

  /**
   * Constructor to initialize User with id, name, and email.
   * 
   * @param id    the user id
   * @param name  the user name
   * @param email the user email
   */
  public User(int id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }

  /**
   * Constructor to initialize User with name, email, password, role, and salt.
   * 
   * @param name     the user name
   * @param email    the user email
   * @param password the user password
   * @param role     the user role
   * @param salt     the salt for password hashing
   */
  public User(String name, String email, String password, String role, String salt) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
    this.salt = salt;
  }

  /**
   * Constructor to initialize User with id, name, email, and role.
   * 
   * @param id    the user id
   * @param name  the user name
   * @param email the user email
   * @param role  the user role
   */
  public User(int id, String name, String email, String role) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.role = role;
  }

  /**
   * Constructor to initialize User with id, name, email, password, role, and
   * salt.
   * 
   * @param id       the user id
   * @param name     the user name
   * @param email    the user email
   * @param password the user password
   * @param role     the user role
   * @param salt     the salt for password hashing
   */
  public User(int id, String name, String email, String password, String role, String salt) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
    this.salt = salt;
  }

  /**
   * Gets the user role.
   * 
   * @return the user role
   */
  public String getRole() {
    return role;
  };

  // Getters và Setters
  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  public String getSalt() {
    return salt;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  /**
   * Returns a string representation of the user.
   * 
   * @return a string representation of the user
   */
  @Override
  public String toString() {
    return id + "," + name + "," + email + "," + role;
  }
}
