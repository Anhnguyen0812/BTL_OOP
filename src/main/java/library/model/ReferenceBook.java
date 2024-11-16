package library.model;

public class ReferenceBook extends Book {

    private static final String referenceCategories = "Reference";

    public ReferenceBook(
            int id,
            String title,
            String author,
            String description,
            String imageUrl,
            String isbn,
            boolean availability) {
        super(id, title, author, isbn, availability, description, imageUrl);
    }

    public ReferenceBook(
            int id,
            String title,
            String author,
            String isbn,
            boolean available) {
        super(id, title, author, isbn, available);
    }

    public ReferenceBook(
            String title,
            String author,
            String isbn,
            String description,
            String imageUrl,
            String QRcode) {
        super(title, author, isbn, description, imageUrl, QRcode);
    }

    // Duplicate constructor removed
    @Override
    public String getCategories() {
        return referenceCategories;
    }
}
