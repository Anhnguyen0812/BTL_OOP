package library.model;

public class ReferenceBook extends Book {
    public ReferenceBook(int id, String title, String author, String isbn, boolean available) {
        super(id, title, author, isbn, available);
    }

    @Override
    public String getType() {
        return "Reference Book";
    }
}

