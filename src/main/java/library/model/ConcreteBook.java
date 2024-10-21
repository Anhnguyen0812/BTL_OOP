package library.model;

public class ConcreteBook extends Book {
    private String publisher;
    private int year;

    public ConcreteBook(int id, String title, String author, String description, String imageUrl, String publisher,
            int year, String isbn, boolean availability) {
        super(id, title, author, isbn, availability, description, imageUrl);
        this.publisher = publisher;
        this.year = year;
    }

    public ConcreteBook(int id, String title, String author, String isbn, int year, String publisher,
            boolean available) {
        super(id, title, author, isbn, available);
        this.publisher = publisher;
        this.year = year;
    }

    public ConcreteBook(String title, String author, String isbn, String description, String imageUrl, String QRcode) {
        super(title, author, isbn, description, imageUrl, QRcode);
    }

    public ConcreteBook(int id, String title, String author, String isbn, boolean available, String description,
            String imageUrl, String QRcode) {
        super(id, title, author, isbn, available, description, imageUrl, QRcode);
    }

    @Override
    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String getType() {
        return "ConcreteBook";
    }

    @Override
    public String toString() {
        return title + " by " + author + " ISBN :" + isbn;
    }
}
