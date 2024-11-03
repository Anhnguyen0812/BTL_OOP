package library.model;

public class ConcreteBook extends Book {
  private final String concreteCategories = "else";

  public ConcreteBook(
      int id,
      String title,
      String author,
      String description,
      String imageUrl,
      String isbn,
      boolean availability) {
    super(id, title, author, isbn, availability, description, imageUrl);
  }

  public ConcreteBook(
      int id,
      String title,
      String author,
      String isbn,
      boolean available) {
    super(id, title, author, isbn, available);
  }

  public ConcreteBook(
      String title,
      String author,
      String isbn,
      String categories,
      String description,
      String imageUrl,
      String QRcode) {
    super(title, author, isbn, categories, description, imageUrl, QRcode);
  }

  public ConcreteBook(
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

  public ConcreteBook(
      int id,
      String title,
      String author,
      String isbn,
      boolean available,
      String categories,
      String description,
      String imageUrl,
      String QRcode) {
    super(id, title, author, isbn, available, categories, description, imageUrl, QRcode);
  }


  @Override
  public String getCategories() {
    return this.categories;
  }

  @Override
  public String toString() {
    return "ConcreteBook{" + "categories='" + concreteCategories + '\'' + '}';
  }
}
