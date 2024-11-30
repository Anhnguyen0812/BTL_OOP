package library.model;

public class ConcreteBook extends Book {

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
      int available) {
    super(id, title, author, isbn, available);
  }

  public ConcreteBook(
      String title,
      String author,
      String isbn,
      String description,
      String imageUrl,
      String QRcode) {
    super(title, author, isbn, description, imageUrl, QRcode);
  }

  public ConcreteBook(
      int id,
      String title,
      String author,
      String isbn,
      int available,
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
      int available,
      String description,
      String imageUrl,
      String QRcode,
      Double rate_avg) {
    super(id, title, author, isbn, available, description, imageUrl, QRcode, rate_avg);
  }

  @Override
  public String getCategories() {
    return categories;
  }

  @Override
  public void setCategories(String categories) {
    this.categories = categories;
  }

  @Override
  public void setRateAvg(Double rateAvg) {
    this.rate_avg = rateAvg;
  }

  @Override
  public String toString() {
    return String.format(
        "ConcreteBook{id=%d, title='%s', author='%s', imageUrl='%s', isbn='%s', availability=%s}",
        this.id, this.title, this.author, this.imageUrl, this.isbn, this.available);

  }
}
