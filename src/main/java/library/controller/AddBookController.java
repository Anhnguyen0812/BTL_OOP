// package library.controller;

// import java.io.File;

// import javax.sound.sampled.AudioInputStream;
// import javax.sound.sampled.AudioSystem;
// import javax.sound.sampled.Clip;

// import com.gluonhq.impl.charm.a.b.b.s;

// import javafx.fxml.FXML;
// import library.dao.BookDAO;
// import library.model.Book;
// import library.model.ISBNScannerWithUI;
// import library.model.ConcreteBook;
// import javafx.scene.control.Button;
// import javafx.scene.control.TextField;
// import javafx.scene.image.ImageView;
// import javafx.scene.control.Label;
// import javafx.scene.image.Image;
// import javafx.scene.layout.Pane;
// import javafx.scene.media.Media;
// import javafx.scene.media.MediaPlayer;

// public class AddBookController {
//     private Book book1, book2;

//     @FXML
//     private Button AddButton, cancelScanButton;

//     @FXML
//     private TextField imgUrl, title, author, isbn, category, description, rateAvg, avaible, availbleScan;
//     BookDAO bookDAO = BookDAO.getBookDAO();

//     @FXML
//     ImageView imageBook;

//     @FXML
//     private Label title2, author2, isbn2, description2, category2, isbnFind;
//     @FXML
//     private Pane root;
//     ISBNScannerWithUI scanner = new ISBNScannerWithUI();

//     public void initialize() {
//         cancelScanButton.setVisible(false);
//     }

//     @FXML
//     public void gotoFindBook() throws Exception {
//         BookController bookController = new BookController();
//         book2 = bookController.searchBookByISBN(isbnFind.getText());
//         if (book2 != null) {
//             imageBook.setImage(new Image(book2.getImageUrl(), true));
//             title2.setText(book2.getTitle());
//             author2.setText(book2.getAuthor());
//             isbn2.setText(book2.getIsbn());
//             description2.setText(book2.getDescription());
//             category2.setText(book2.getCategories());
//         } else {
//             System.out.println("Book not found");
//         }
//     }

//     @FXML
//     public void gotoScanBook() throws Exception {
//         cancelScanButton.setVisible(true);
//         scanner.show(root);
//         scanner.setOnScanCompleteListener((isbntext) -> {
//             isbnFind.setText("Isbn: " + isbntext);
//             try {
//                 BookController bookController = new BookController();
//                 book2 = bookController.searchBookByISBN(isbntext);
//                 // Play success sound
//                 File audioFile = new File("src/main/resources/sound/success.wav");
//                 AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

//                 // Tạo và mở clip
//                 Clip clip = AudioSystem.getClip();
//                 clip.open(audioStream);

//                 // Phát âm thanh
//                 clip.start();
//             } catch (Exception e) {
//                 e.printStackTrace();
//             }

//             if (book2 != null) {
//                 if (book2.getImageUrl() != null)
//                     imageBook.setImage(new Image(book2.getImageUrl(), true));
//                 else
//                     imageBook.setImage(new Image("/imgs/noBook.png", true));
//                 title2.setText(book2.getTitle());
//                 author2.setText(book2.getAuthor());
//                 isbn2.setText(book2.getIsbn());
//                 description2.setText(book2.getDescription());
//                 category2.setText(book2.getCategories());
//             } else {
//                 System.out.println("Book not found");
//             }
//         });

//     }

//     @FXML
//     public void gotoAddBook1() throws Exception {

//         if (title.getText().equals("") || author.getText().equals("") || isbn.getText().equals("")
//                 || description.getText().equals("") || imgUrl.getText().equals("")
//                 || availbleScan.getText().equals("")) {
//             System.out.println("Please fill all the fields");
//             return;
//         } else
//             book1 = new ConcreteBook(title.getText(), author.getText(), isbn.getText(), description.getText(),
//                     imgUrl.getText(), availbleScan.getText());

//         if (book1 != null)
//             bookDAO.addBook(book1);
//         else
//             System.out.println("Book is null");
//     }

//     @FXML
//     public void gotoAddBook2() throws Exception {

//         if (book2 != null)
//             bookDAO.addBook(book2);
//         else
//             System.out.println("Book is null");
//         scanner.stopCamera();
//     }

//     @FXML
//     public void gotoCancelScan() throws Exception {
//         if (scanner != null)
//             scanner.stopCamera();
//         root.getChildren().clear();
//         cancelScanButton.setVisible(false);
//     }

// }
