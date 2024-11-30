package library.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javafx.scene.image.Image;
import library.dao.UserDAO;

public class UserQRCode {

    public static Image generateQRCode(User user) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(user.toString(), BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(pngData);
            return new Image(inputStream);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User decodeQRCode(String qrCode) throws SQLException {
        String id = qrCode.split(",")[0];
        // String name = qrCode.split(",")[1];
        // String email = qrCode.split(",")[2];
        UserDAO userDAO = UserDAO.getUserDAO();
        return userDAO.getUserById(Integer.parseInt(id));
    }
}
