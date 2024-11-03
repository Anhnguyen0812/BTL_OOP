package library.model;

public abstract class Book {
  protected int id;
  protected String title;
  protected String author;
  protected String isbn;
  protected boolean available;
  protected String categories;
  protected String description; // Thêm thuộc tính mô tả
  protected String imageUrl; // Thêm thuộc tính URL hình ảnh
  protected String QRcode; // Thêm thuộc tính QRcode

  public Book(
      int id,
      String title,
      String author,
      String isbn,
      boolean available,
      String description,
      String imgaeUrl) {}

  public Book(int id, String title, String author, String isbn, boolean available) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.isbn = isbn;
    this.available = available;
  }

  public Book(
      String title,
      String author,
      String isbn,
      String categories,
      String description,
      String imageUrl,
      String QRcode) {
    this.title = title;
    this.author = author;
    this.isbn = isbn;
    this.categories = categories;
    this.description = description;
    this.imageUrl = imageUrl;
    this.QRcode = QRcode;
  }

  public Book(
      int id,
      String title,
      String author,
      String isbn,
      boolean available,
      String description,
      String imageUrl,
      String QRcode) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.isbn = isbn;
    this.available = available;
    this.description = description;
    this.imageUrl = imageUrl;
    this.QRcode = QRcode;
  }

  public Book(
      int id,
      String title,
      String author,
      String isbn,
      boolean available,
      String categories,
      String description,
      String imageUrl,
      String QRcode) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.isbn = isbn;
    this.available = available;
    this.categories = categories;
    this.description = description;
    this.imageUrl = imageUrl;
    this.QRcode = QRcode;
  }
  
  // Getters và Setters
  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public String getIsbn() {
    return isbn;
  }

  public String getDescription() {
    return description;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getQRcode() {
    return QRcode;
  }

  public boolean isAvailable() {
    return available;
  }

  public abstract String getCategories();


 public void setAvailable(boolean available) {
    this.available = available;
  } 
}
