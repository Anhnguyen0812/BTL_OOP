package library.model;

public class Member extends User {
    public Member(int id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public String getRole() {
        return "Member";
    }
}

