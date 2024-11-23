package library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import library.model.Book;
import library.model.ConcreteBook;
import library.util.DBConnection;

public class BookReviewDAO {
    private final Connection connection;
    private static BookReviewDAO instance;

    private BookReviewDAO() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public static BookReviewDAO getBookReviewDao() {
        if (instance == null) {
            instance = new BookReviewDAO();
        }
        return instance;
    }

    public void addReview(int bookId, int userId, String review, int rate) throws SQLException {
        String query = "INSERT INTO book_review (book_id, user_id, review, rate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setString(3, review);
            preparedStatement.setInt(4, rate);
            preparedStatement.executeUpdate();
        }
    }

    public void updateReview(int reviewId, String review, int rate) throws SQLException {
        String query = "UPDATE book_review SET review = ?, rate = ? WHERE review_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, review);
            preparedStatement.setInt(2, rate);
            preparedStatement.setInt(3, reviewId);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteReview(int reviewId) throws SQLException {
        String query = "DELETE FROM book_review WHERE review_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, reviewId);
            preparedStatement.executeUpdate();
        }
    }
}
