package library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.util.Pair;
import library.util.DBConnection;

public class BookReviewDAO implements DAO {
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

    public void updateReview(int userId, int bookId, String review, int rate) throws SQLException {
        String query = "UPDATE book_review SET review = ?, rate = ? WHERE book_id = ? AND user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, review);
            preparedStatement.setInt(2, rate);
            preparedStatement.setInt(3, bookId);
            preparedStatement.setInt(4, userId);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteReview(int userId, int bookId) throws SQLException {
        String query = "DELETE FROM book_review WHERE book_id = ? AND user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookId);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        }
    }

    public Pair<String, Double> getReviewBook(int bookId) throws SQLException {
        String query = "SELECT review, rate FROM book_review WHERE book_id = ? ORDER BY rate DESC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            StringBuffer review = new StringBuffer();
            double rate = 0;
            int cnt = 0;
            while (resultSet.next()) {
                review.append(resultSet.getString("review")).append(',');
                rate += resultSet.getInt("rate");
                if (rate != 0) {
                    cnt++;
                }
            }
            if (cnt != 0) {
                rate /= cnt;
            }

            return new Pair<>(review.toString(), rate);
        }
    }

    public boolean getReviewBookByUser(int userId, int bookId) throws SQLException {
        String query = "SELECT * FROM book_review WHERE book_id = ? AND user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, bookId);
        preparedStatement.setInt(2, userId);
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }

    public String getCommentBook(int bookId) throws SQLException {
        String query = "SELECT review FROM book_review WHERE book_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            StringBuffer review = new StringBuffer();
            while (resultSet.next()) {
                review.append(resultSet.getString("review")).append(',');
            }
            return review.toString();
        }
    }
}
