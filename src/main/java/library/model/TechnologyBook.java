package library.model;

public class TechnologyBook extends Book {
    private final String technologyCategories = "Technology";

    public TechnologyBook(
        int id,
        String title,
        String author,
        String description,
        String imageUrl,
        String isbn,
        boolean availability) {
        super(id, title, author, isbn, availability, description, imageUrl);
    }

    public TechnologyBook(
        int id,
        String title,
        String author,
        String isbn,
        boolean available) {
        super(id, title, author, isbn, available);
    }

    public TechnologyBook(
        String title,
        String author,
        String isbn,
        String description,
        String imageUrl,
        String QRcode) {
        super(title, author, isbn, description, imageUrl, QRcode);
    }


    public TechnologyBook(
        int id,
        String title,
        String author,
        String isbn,
        boolean available,
        String description,
        String imageUrl,
        String QRcode) {
        super(id, title, author, isbn, available, description, imageUrl, QRcode);
    }

    @Override
    public String getCategories() {
        return technologyCategories;
    }
}
