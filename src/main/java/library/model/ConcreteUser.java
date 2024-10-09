package library.model;

public class ConcreteUser extends User {
    public ConcreteUser(int id, String name, String email) {
        super(id, name, email);
    }

    public ConcreteUser(String name, String email, String password, String role) {
        super(name, email, password, role);
    }
    
    @Override
    public String getRole() {
        return "";
    }
    
}
