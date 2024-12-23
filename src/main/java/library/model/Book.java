package library.model;

import com.gluonhq.impl.charm.a.a.a;

public abstract class Book {
  protected int id;
  protected String title;
  protected String author;
  protected String isbn;
  protected int available;
  protected String description; // Thêm thuộc tính mô tả
  protected String categories; // Thêm thuộc tính danh mục
  protected String imageUrl; // Thêm thuộc tính URL hình ảnh
  protected String QRcode; // Thêm thuộc tính QRcode
  protected Double rate_avg; // Thêm thuộc tính rate_avg

  public Book(
      int id,
      String title,
      String author,
      String isbn,
      int available,
      String genre,
      String imgaeUrl) {
  }

  public Book(int id, String title, String author, String isbn, int available) {
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
      String description,
      String imageUrl,
      String QRcode) {
    this.title = title;
    this.author = author;
    this.isbn = isbn;
    this.description = description;
    this.imageUrl = imageUrl;
    this.QRcode = QRcode;
  }

  public Book(
      int id,
      String title,
      String author,
      String isbn,
      int available,
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
      int available,
      String description,
      String imageUrl,
      String QRcode,
      Double rate_avg) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.isbn = isbn;
    this.available = available;
    this.description = description;
    this.imageUrl = imageUrl;
    this.QRcode = QRcode;
    this.rate_avg = rate_avg;
  }

  public Book(
      int id,
      String title,
      String author,
      String isbn,
      int available,
      String description,
      String imageUrl,
      String QRcode,
      String categories) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.isbn = isbn;
    this.available = available;
    this.description = description;
    this.imageUrl = imageUrl;
    this.QRcode = QRcode;
    this.categories = categories;
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
    return available > 0;
  }

  public String getCategories() {
    return categories;
  }

  public void setCategories(String categories) {
    this.categories = categories;
  }

  public void setAvailable(int available) {
    this.available = available;
  }

  public int getAvailable() {
    return available;
  }

  public void setRateAvg(Double rate_avg) {
    this.rate_avg = rate_avg;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setQRcode(String qRcode) {
    QRcode = qRcode;
  }

  public Double getRateAvg() {
    return rate_avg;
  }

}
