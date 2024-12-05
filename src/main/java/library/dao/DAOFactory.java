package library.dao;

public class DAOFactory {
    public static DAO getDAO(DAOType daoType) {
        switch (daoType) {
            case ALL_DAO:
                return new AllDao();
            case BORROW_RECORD_DAO:
                return BorrowRecordDAO.getBorrowRecordDAO();
            case BOOK_REVIEW_DAO:
                return BookReviewDAO.getBookReviewDao();
            case BOOK_DAO:
                return BookDAO.getBookDAO();
            case USER_DAO:
                return UserDAO.getUserDAO();
            case NOTI_DAO:
                return NotiDAO.geNotiDAO();
            default:
                throw new IllegalArgumentException("Unknown DAO type");
        }
    }

    public enum DAOType {
        ALL_DAO,
        BORROW_RECORD_DAO,
        BOOK_REVIEW_DAO,
        BOOK_DAO,
        NOTI_DAO,
        USER_DAO
    }
}
