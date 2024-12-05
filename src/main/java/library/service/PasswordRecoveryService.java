package library.service;

import java.util.Properties;
import java.util.Random;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Dịch vụ phục hồi mật khẩu cho người dùng. Cung cấp các phương thức để tạo mã xác nhận, lưu trữ
 * token vào cơ sở dữ liệu, kiểm tra tính hợp lệ của token và gửi mã xác nhận qua email.
 */
public class PasswordRecoveryService {

    public static boolean sendEmail(String recipient, String subject, String content) {
    boolean ok = true;

    // Cấu hình các thuộc tính SMTP
    Properties properties = new Properties();
    properties.put("mail.smtp.host", "smtp.gmail.com"); // Máy chủ SMTP
    properties.put("mail.smtp.port", "587"); // Cổng SMTP
    properties.put("mail.smtp.auth", "true"); // Bật xác thực
    properties.put("mail.smtp.starttls.enable", "true"); // Bật STARTTLS

    // Thông tin đăng nhập
    String email = "json00906@gmail.com"; // Email của bạn
    String appPassword = "ofnm kjvt vgen vszq"; // Mật khẩu ứng dụng

    // Tạo phiên làm việc với xác thực
    Session session = Session.getInstance(properties, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(email, appPassword);
      }
    });

    try {
      // Tạo nội dung email
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(email)); // Địa chỉ người gửi
      message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); // Người nhận
      message.setSubject(subject); // Chủ đề email
      message.setText(content); // Nội dung email

      // Gửi email
      Transport.send(message);
      System.out.println("Email sent successfully to " + recipient);
      ok = true;
    } catch (MessagingException e) {
      e.printStackTrace();
      System.err.println("Failed to send email: " + e.getMessage());
      ok = false;
    }
    return ok;
  }

  /**
   * Tạo mã xác nhận ngẫu nhiên gồm 4 chữ số.
   *
   * @return Mã xác nhận ngẫu nhiên.
   */
  public static String randomCode() {
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 4; i++) {
      int digit = random.nextInt(10); // Tạo số ngẫu nhiên từ 0 đến 9
      sb.append(digit);
    }
    return sb.toString();
  }

  /**
   * Gửi mã xác nhận phục hồi mật khẩu đến email của người dùng.
   *
   * @param username Tên người dùng yêu cầu phục hồi mật khẩu.
   * @return true nếu gửi email thành công, false nếu có lỗi xảy ra.
   */
  public static boolean sendToken(String username) {
    String token = randomCode();
    System.out.println(token); // In mã token ra console (có thể thay bằng logging thực tế)
    return sendEmail(username, "Mã xác nhận là: ", token);
  }

}