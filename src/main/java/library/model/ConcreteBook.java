package library.model;

public class ConcreteBook extends Book {
  private final String concreteCategories = "Else";

  public ConcreteBook(
      int id,
      String title,
      String author,
      String description,
      String imageUrl,
      String isbn,
      boolean available) {
    super(id, title, author, isbn, available, description, imageUrl);
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
    return String.format(
        "ConcreteBook{id=%d, title='%s', author='%s', description='%s', imageUrl='%s', isbn='%s', availability=%s}",
        this.id, this.title, this.author, this.description, this.imageUrl, this.isbn, this.available);
  
  }
}
