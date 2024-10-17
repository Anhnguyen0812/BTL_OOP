package library.model;

public class EBook extends Book {
    private String downloadLink;

    public EBook(int id, String title, String author, String isbn, boolean available, String downloadLink) {
        super(id, title, author, isbn, available);
        this.downloadLink = downloadLink;
    }

    @Override
    public String getType() {
        return "E-Book";
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }
}