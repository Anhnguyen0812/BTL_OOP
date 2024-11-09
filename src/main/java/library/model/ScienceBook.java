package library.model;

public class ScienceBook extends Book {
    private final String scienceCategories = "Science";
    
    public ScienceBook(
        int id,
        String title,
        String author,
        String description,
        String imageUrl,
        String isbn,
        boolean availability) {
        super(id, title, author, isbn, availability, description, imageUrl);
    }
    
    public ScienceBook(
        int id,
        String title,
        String author,
        String isbn,
        boolean available) {
        super(id, title, author, isbn, available);
    }
    
    public ScienceBook(
        String title,
        String author,
        String isbn,
        String description,
        String imageUrl,
        String QRcode) {
        super(title, author, isbn, description, imageUrl, QRcode);
    }
    
    // Duplicate constructor removed
    
    public ScienceBook(
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
        return scienceCategories;
    }
}
