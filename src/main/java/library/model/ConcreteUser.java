package library.model;

public class ConcreteUser extends User {
    public ConcreteUser(int id, String name, String email) {
        super(id, name, email);
    }

    public ConcreteUser(String name, String email, String password, String role, String salt) {
        super(name, email, password, role, salt);
    }

    public ConcreteUser(int id, String name, String email, String password, String role, String salt) {
        super(id, name, email, password, role, salt);
    }

    @Override
    public String getRole() {
        return "";
    }

}
