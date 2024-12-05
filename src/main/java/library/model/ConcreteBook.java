package library.model;

/**
 * ConcreteBook class extends Book to provide specific book implementations.
 */
public class ConcreteBook extends Book {

  /**
   * Constructor to initialize ConcreteBook with id, title, author, description,
   * imageUrl, isbn, and availability.
   * 
   * @param id          the book id
   * @param title       the book title
   * @param author      the book author
   * @param description the book description
   * @param imageUrl    the book image URL
   * @param isbn        the book ISBN
   * @param available   the book availability
   */
  public ConcreteBook(
      int id,
      String title,
      String author,
      String description,
      String imageUrl,
      String isbn,
      int available) {
    super(id, title, author, isbn, available, description, imageUrl);
  }

  /**
   * Constructor to initialize ConcreteBook with id, title, author, isbn, and
   * availability.
   * 
   * @param id        the book id
   * @param title     the book title
   * @param author    the book author
   * @param isbn      the book ISBN
   * @param available the book availability
   */
  public ConcreteBook(
      int id,
      String title,
      String author,
      String isbn,
      int available) {
    super(id, title, author, isbn, available);
  }

  /**
   * Constructor to initialize ConcreteBook with title, author, isbn, description,
   * imageUrl, and QR code.
   * 
   * @param title       the book title
   * @param author      the book author
   * @param isbn        the book ISBN
   * @param description the book description
   * @param imageUrl    the book image URL
   * @param QRcode      the book QR code
   */
  public ConcreteBook(
      String title,
      String author,
      String isbn,
      String description,
      String imageUrl,
      String QRcode) {
    super(title, author, isbn, description, imageUrl, QRcode);
  }

  /**
   * Constructor to initialize ConcreteBook with id, title, author, isbn,
   * availability, description, imageUrl, and QR code.
   * 
   * @param id          the book id
   * @param title       the book title
   * @param author      the book author
   * @param isbn        the book ISBN
   * @param available   the book availability
   * @param description the book description
   * @param imageUrl    the book image URL
   * @param QRcode      the book QR code
   */
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

  /**
   * Constructor to initialize ConcreteBook with id, title, author, isbn,
   * availability, description, imageUrl, QR code, and average rating.
   * 
   * @param id          the book id
   * @param title       the book title
   * @param author      the book author
   * @param isbn        the book ISBN
   * @param available   the book availability
   * @param description the book description
   * @param imageUrl    the book image URL
   * @param QRcode      the book QR code
   * @param rate_avg    the book average rating
   */
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

  /**
   * Gets the book categories.
   * 
   * @return the book categories
   */
  @Override
  public String getCategories() {
    return categories;
  }

  /**
   * Sets the book categories.
   * 
   * @param categories the book categories
   */
  @Override
  public void setCategories(String categories) {
    this.categories = categories;
  }

  /**
   * Sets the book average rating.
   * 
   * @param rateAvg the book average rating
   */
  @Override
  public void setRateAvg(Double rateAvg) {
    this.rate_avg = rateAvg;
  }

  /**
   * Returns a string representation of the book.
   * 
   * @return a string representation of the book
   */
  @Override
  public String toString() {
    return String.format(
        "ConcreteBook{id=%d, title='%s', author='%s', imageUrl='%s', isbn='%s', availability=%s}",
        this.id, this.title, this.author, this.imageUrl, this.isbn, this.available);

  }
}
