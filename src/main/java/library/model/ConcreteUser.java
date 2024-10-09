package library.model;

public class ConcreteUser extends User {
    public ConcreteUser(int id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public String getRole() {
        return "";
    }
    
}
