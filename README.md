# Library Management System  

## Giới thiệu  
**Library Management System** là một ứng dụng Java được phát triển với JavaFX và SQLite để quản lý các hoạt động thư viện như mượn, trả sách và quản lý người dùng. Dự án sử dụng kiến trúc **Abstract Factory Pattern** để tạo ra giao diện chung cho các vai trò người dùng và quản trị viên.  

## Tính năng chính  
- **Giao diện người dùng:**  
  - Đăng nhập/Đăng xuất.  
  - Quản lý thông tin cá nhân.  
- **Giao diện quản trị viên:**  
  - Quản lý sách (thêm, sửa, xóa).  
  - Quản lý người dùng (thêm, sửa, xóa).  
  - Xem thống kê mượn sách.  
- **Tìm kiếm sách:**  
  - Tìm kiếm theo tên, tác giả, hoặc thể loại.  
  - Đồng bộ dữ liệu với Google Books API.  
- **Quản lý mượn trả sách:**  
  - Ghi nhận lịch sử mượn trả.  
  - Kiểm tra tình trạng sách (đang mượn/có sẵn).  

## Yêu cầu hệ thống  
- **Hệ điều hành:** macOS 12.7 hoặc cao hơn.  
- **Java:** JDK 23 trở lên.  
- **Công cụ phát triển:** Visual Studio Code với Java Extension Pack.  
- **Cơ sở dữ liệu:** SQLite.  

## Cài đặt  
1. Clone repository:  
   ```bash  
   git clone https://github.com/username/library-management-system.git  
   cd library-management-system  
    javac -cp .:lib/javafx-sdk/lib/*.jar src/*.java  
    java -cp .:lib/javafx-sdk/lib/*.jar src.Main  
2. **Cách cài đặt**
   - Clone repo: `git clone https://github.com/Anhnguyen0812/BTL_OOP.git`
   - Add project vào IDE và sử dụng
## Cấu trúc dự án
LibraryManagementSystem/<br>
├── src/<br>
│   ├── main/<br>
│   │   ├── java/<br>
│   │   │   ├── com/library/<br>
│   │   │   │   ├── controller/<br>
│   │   │   │   │   ├── LibraryController.java<br>
│   │   │   │   │   ├── BookController.java<br>
│   │   │   │   ├── dao/<br>
│   │   │   │   │   ├── BookDAO.java<br>
│   │   │   │   │   ├── UserDAO.java<br>
│   │   │   │   │   ├── BorrowRecordDAO.java<br>
│   │   │   │   ├── model/<br>
│   │   │   │   │   ├── Book.java<br>
│   │   │   │   │   ├── ReferenceBook.java<br>
│   │   │   │   │   ├── EBook.java<br>
│   │   │   │   │   ├── User.java<br>
│   │   │   │   │   ├── Librarian.java<br>
│   │   │   │   │   ├── Member.java<br>
│   │   │   │   │   ├── BorrowRecord.java<br>
│   │   │   │   ├── service/<br>
│   │   │   │   │   ├── BookService.java<br>
│   │   │   │   │   ├── BorrowService.java<br>
│   │   │   │   │   ├── UserService.java<br>
│   │   │   │   ├── util/<br>
│   │   │   │   │   ├── DBConnection.java<br>
│   │   │   │   ├── api/<br>
│   │   │   │   │   ├── GoogleBooksAPI.java<br>
│   │   │   ├── resources/<br>
│   │   │   │   ├── styles/<br>
│   │   │   │   │   ├── app.css<br>
│   │   │   │   ├── fxml/<br>
│   │   │   │   │   ├── main.fxml<br>
│   └── test/<br>
├── lib/<br>
├── .gitignore<br>
├── README.md<br>
├── build.gradle or pom.xml<br>
└── config/<br>
    ├── application.properties<br>

## Hướng dẫn sử dụng
### Đăng nhập / Đăng ký
- Đăng nhập, đăng ký sử dụng tài khoản email của bạn. Hãy chắc chắn rằng email chính xác vì nó sử dụng lúc bạn quên mật khẩu
- Mật khẩu được mã hóa theo chuẩn thuật toán Bycrypt để tăng tính bảo mật
  <details close>
    <summary><samp>UI Đăng nhập, Đăng ký, Quên mật khẩu</samp></summary>
    <br>
    Đăng nhập:
    <br>
    <img src="https://github.com/user-attachments/assets/ef84e043-6104-4301-9f23-92db609c1d60" alt="UI Đăng nhập" width="1000">
    <br>
    Đăng ký:
    <br>
    <img src="https://github.com/user-attachments/assets/57b48d69-d5f4-47c8-85ec-0712507705dc" alt="UI Đăng ký" width="1000">
    <br>
    Gửi mã xác nhận về email:
    <br>
    <img src="https://github.com/user-attachments/assets/03ad22c1-db88-4658-a195-da03a18fa212" alt="UI Reset Password" width="1000">
    <br>
  </details>
### Giao diện người dùng
  <details close>
    <summary><samp>Dashboard hiển thể thể loại sách, số lượt truy cập ...</samp></summary>
    <br>
    <img src="https://github.com/user-attachments/assets/7076abad-1d27-4a0e-ab92-be67219b98a2" alt="UI Reset Password" width="1000">
  </details>

  <details close>
    <summary><samp>Tìm kiếm sách từ thư viện hoặc API</samp></summary>
    <br>
    <img src="https://github.com/user-attachments/assets/670a38c7-31a1-45da-947c-6e6273bb541b" alt="UI Reset Password" width="1000">
  </details>
  <details close>
    <summary><samp>Xem chi tiết sách mượn, xóa, sửa, thêm vào thư viện</samp></summary>
    <br>
    <img src="https://github.com/user-attachments/assets/49314c7c-41b4-4f30-97b6-628cb2a09309" alt="UI Reset Password" width="1000">
  </details>
  <details close>
    <summary><samp>Cho phép bình luận, rating về sách</samp></summary>
    <br>
    <img src="https://github.com/user-attachments/assets/b39344e3-3495-418e-93f4-64eb55988325" alt="UI Reset Password" width="1000">
  </details>
  <details close>
    <summary><samp>Hệ thống quản lý sinh viên</samp></summary>
    <br>
    <img src="https://github.com/user-attachments/assets/5ce7dbfb-1e7a-4280-add0-e60aab50bd17" alt="UI Reset Password" width="1000">
  </details>
  <details close>
    <summary><samp>Hệ thống quản lý thông báo</samp></summary>
    <br>
    <img src="https://github.com/user-attachments/assets/e83885a8-c937-4fb7-ac64-3d7c01e2e7e7" alt="UI Reset Password" width="1000">
  </details>
  <details close>
    <summary><samp>Hệ thống chỉnh sửa thông tin cá nhân, đổi mật khẩu, UI Setting</samp></summary>
    <br>
    <img src="https://github.com/user-attachments/assets/a7a1e45e-c5cb-42e8-8d01-2d2054235ee1" alt="UI Reset Password" width="1000">
  </details>

## Sử dụng các nguyên tắc lập trình OOP,  Design Pattern
- Sử dụng các design pattern như Singleton, Abstract Factory, DAO ...
- Tích hợp đa luồng để cải thiện hiệu suất của chương trình
- ...

## Thành viên
| **Họ và tên**        | **Mã sinh viên** |
|-----------------------|------------------|
| Hoàng Quốc Bảo        | 23020012         |
| Nguyễn Phi Anh        | 23020009         |

## Kế hoạch phát triển
- **Phiên bản 1.0:** Hoàn thiện các tính năng cơ bản và giao diện người dùng (Done)
<<<<<<< HEAD
- **Phiên bản 2.0:** Tăng cường hiệu suất, bảo mật và thêm các tính năng nâng cao hơn, ... (Donate to use...)
