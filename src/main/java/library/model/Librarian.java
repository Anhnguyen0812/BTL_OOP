package library.model;

public class Librarian extends User {
    public Librarian(int id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public String getRole() {
        return "Librarian";
    }
}
