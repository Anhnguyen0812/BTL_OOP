package library.model;

public class User {
  protected int id = 0;
  protected String name;
  protected String email;
  protected String role;
  protected String password;
  protected String salt;

  public User(int id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }

  public User(String name, String email, String password, String role, String salt) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
    this.salt = salt;
  }

  public User(int id, String name, String email, String role) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.role = role;
  }

  public User(int id, String name, String email, String password, String role, String salt) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
    this.salt = salt;
  }

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
}
