

CREATE DATABASE java5_assignment CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE java5_assignment;

-- Categories (Loại)
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

-- Publishers (Nhà xuất bản )
CREATE TABLE publishers (
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    contact_email VARCHAR(100),
    PRIMARY KEY (id)
);

-- Accounts (Người dùng & Admin)
CREATE TABLE accounts (
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fullname VARCHAR(100) NOT NULL,
    avatar VARCHAR(255),
    admin BOOLEAN DEFAULT FALSE, -- TRUE: Admin, FALSE: User
    enabled BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (email)
);

select * from accounts;
-- 5. Books (Sách )
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT NOT NULL,
    title VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    price DECIMAL(10, 2) NOT NULL,
    quantity INT DEFAULT 0, -- Tồn kho
    description TEXT, -- Mô tả chi tiết

    -- (Book details)
    publish_date DATE,
    dimensions VARCHAR(50), -- Kích thước (VD: 14x20cm)
    translator VARCHAR(100), -- Dịch giả
    cover_type VARCHAR(50), -- Loại bìa (Cứng/Mềm)
    page_count INT,
    author_name VARCHAR(200),
    is_ebook BOOLEAN DEFAULT FALSE, -- Hỗ trợ Ebook
    available BOOLEAN DEFAULT TRUE,

    -- Khóa ngoại
    category_id BIGINT,
    publisher_id BIGINT,

    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (publisher_id) REFERENCES publishers(id)
);

-- Orders (Đơn hàng)
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT NOT NULL,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    address VARCHAR(255) NOT NULL,
    status INT DEFAULT 0, -- 0: Mới, 1: Đang giao, 2: Hoàn tất, 3: Hủy
    email VARCHAR(100),
    phone  VARCHAR(100),
    shipping_fee DECIMAL(10,2) DEFAULT 0.00,
    note TEXT,
    PRIMARY KEY (id),
    FOREIGN KEY (email) REFERENCES accounts(email)
);

-- OrderDetails (Chi tiết đơn hàng)
CREATE TABLE order_details (
    id BIGINT AUTO_INCREMENT NOT NULL,
    price DECIMAL(10, 2) NOT NULL, -- Giá TẠI THỜI ĐIỂM MUA
    quantity INT NOT NULL,
    book_id BIGINT,
    order_id BIGINT,

    PRIMARY KEY (id),
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE cart (
    id BIGINT AUTO_INCREMENT NOT NULL,
    email VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (email) REFERENCES accounts(email)
);

-- 2. Bảng CartDetails (Các món hàng nằm trong giỏ)
CREATE TABLE cart_details (
    id BIGINT AUTO_INCREMENT NOT NULL,
    cart_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    added_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id)
);


USE java5_assignment;

INSERT INTO accounts (email, password, fullname, avatar, admin) VALUES
('admin@fpt.edu.vn', 'admin123', 'System Administrator', 'admin.png', TRUE),
('thaihoc@gmail.com', 'admin', 'Thái Học', 'admin.png', TRUE),
('Hoc@gmail.com', 'pass123', 'Nguyen Thai Hoc', 'user1.png', FALSE),
('MinhHieu@gmail.com', 'pass123', 'Bui Minh Hieu', 'user2.png', FALSE),
('TrungHieu@gmail.com', 'pass123', 'Huynh Trung Hieu', 'user3.png', FALSE),
('PhuongTram@gmail.com', 'pass123', 'Vu Phuong Tram', 'user4.png', FALSE),
('ThuyVy@gmail.com', 'pass123', 'Vo Thi Thuy Vy', 'user5.png', FALSE);


INSERT INTO categories (id, name) VALUES
(1, 'Công nghệ thông tin'),
(2, 'Kinh tế & Quản trị'),
(3, 'Văn học nghệ thuật'),
(4, 'Kỹ năng sống'),
(5, 'Ngoại ngữ'),
(6, 'Khoa học kỹ thuật'),
(7, 'Tâm lý học'),
(8, 'Sách thiếu nhi'),
(9, 'Lịch sử - Địa lý'),
(10, 'Triết học');


INSERT INTO publishers (name, address, contact_email) VALUES
('NXB Giáo Dục', 'Hà Nội', 'contact@nxb_gd.vn'),
('NXB Trẻ', 'TP.HCM', 'info@nxbtre.com.vn'),
('NXB Kim Đồng', 'Hà Nội', 'kimdong@nxb.vn'),
('NXB Tổng Hợp', 'TP.HCM', 'tonghop@nxbth.vn'),
('NXB Phụ Nữ', 'Hà Nội', 'phunu@nxbpn.vn');

INSERT INTO books 
(id, title, image, price, quantity, description, publish_date, dimensions, translator, cover_type, page_count, author_name, is_ebook, available, category_id, publisher_id) 
VALUES
-- Lập trình (Category 1)
(1, 'Java: How to Program', 'javadeitel.png', 450000, 50, 'Sách giáo trình lập trình Java kinh điển của Deitel', '2020-05-10', '18x25cm', 'Nhiều dịch giả', 'Mềm', 1200, 'Paul Deitel', FALSE, TRUE, 1, 1),
(2, 'Spring Boot in Action', 'springaction.jpg', 285000, 40, 'Hướng dẫn thực hành Spring Boot cho Java Developer', '2019-12-15', '16x24cm', 'Nguyễn Văn A', 'Mềm', 400, 'Craig Walls', FALSE, TRUE, 1, 2),
(9, 'Machine Learning cơ bản', 'mltiep.jpg', 290000, 30, 'Sách học Machine Learning bằng tiếng Việt đầy đủ nhất', '2020-02-20', '16x24cm', NULL, 'Mềm', 450, 'Vũ Hữu Tiệp', FALSE, TRUE, 1, 1),
(10, 'Python Data Science Handbook', 'pythonds.jpg', 320000, 40, 'Sách hướng dẫn khoa học dữ liệu với Python', '2022-07-07', '17x23cm', 'Lê D', 'Mềm', 548, 'Jake VanderPlas', FALSE, TRUE, 1, 2),
(13, 'Introduction to Algorithms', 'clrs.jpg', 550000, 35, 'Cuốn sách gối đầu giường về thuật toán (CLRS)', '2021-10-10', '18x24cm', 'Nhiều dịch giả', 'Mềm', 1312, 'Thomas H. Cormen', FALSE, TRUE, 1, 1),
(14, 'Clean Code', 'cleancode.jpg', 350000, 25, 'Cẩm nang về mã sạch cho lập trình viên chuyên nghiệp', '2018-01-01', '16x24cm', 'Nguyễn E', 'Mềm', 464, 'Robert C. Martin', FALSE, TRUE, 1, 2),
(19, 'SQL Antipatterns', 'sqlanti.jpg', 260000, 45, 'Cách tránh các sai lầm phổ biến khi thiết kế SQL', '2022-05-05', '16x24cm', 'Nguyễn H', 'Mềm', 350, 'Bill Karwin', FALSE, TRUE, 1, 2),
(20, 'Design Patterns: Elements of Reusable Object-Oriented Software', 'gofdp.jpg', 420000, 20, 'Cuốn sách gốc về Design Patterns của nhóm Gang of Four', '2018-08-08', '16x24cm', 'Đội ngũ Kỹ thuật', 'Cứng', 416, 'Erich Gamma', FALSE, TRUE, 1, 2),
(21, 'The Pragmatic Programmer', 'pragmatic.jpg', 380000, 15, 'Hành trình từ thợ học việc đến bậc thầy lập trình', '2019-09-13', '17x23cm', 'Phạm G', 'Mềm', 352, 'Andrew Hunt', FALSE, TRUE, 1, 2),

-- Kinh tế & Quản trị (Category 2)
(3, 'Kinh tế học vi mô', 'microkrugman.jpg', 220000, 30, 'Giáo trình kinh tế học vi mô của giải Nobel Kinh tế', '2021-09-01', '19x24cm', 'Nhóm dịch giả', 'Mềm', 650, 'Paul Krugman', FALSE, TRUE, 2, 1),
(4, 'Quản trị trong thời đại biến động', 'mgmtdrucker.jpg', 195000, 25, 'Tác phẩm kinh điển về quản trị của Peter Drucker', '2020-06-12', '15x23cm', 'Trần Thị B', 'Cứng', 520, 'Peter Drucker', FALSE, TRUE, 2, 4),
(18, 'Marketing căn bản', 'marketingkotler.jpg', 235000, 50, 'Nguyên lý Marketing - Kinh điển của Philip Kotler', '2019-11-11', '20x28cm', 'Trần G', 'Mềm', 750, 'Philip Kotler', FALSE, TRUE, 2, 4),
(22, 'Cha giàu Cha nghèo', 'richdad.jpg', 125000, 100, 'Bài học về tài chính cá nhân', '2017-04-11', '14x20cm', 'Thiên Kim', 'Mềm', 336, 'Robert Kiyosaki', FALSE, TRUE, 2, 2),

-- Văn học & Thiếu nhi (Category 3, 8)
(5, 'Dế Mèn Phiêu Lưu Ký', 'demen.jpg', 85000, 100, 'Tác phẩm văn học thiếu nhi kinh điển nhất Việt Nam', '2022-01-20', '14.5x20.5cm', NULL, 'Mềm', 188, 'Tô Hoài', FALSE, TRUE, 8, 3),
(23, 'Nhà giả kim', 'alchemy.jpg', 79000, 80, 'Tiểu thuyết mang tính triết lý về theo đuổi ước mơ', '2020-03-10', '13x20cm', 'Lê Chu Cầu', 'Mềm', 228, 'Paulo Coelho', FALSE, TRUE, 3, 4),
(24, 'Số Đỏ', 'sodo.jpg', 95000, 40, 'Tác phẩm trào phúng kinh điển của văn học Việt Nam', '2021-05-15', '13x20cm', NULL, 'Mềm', 256, 'Vũ Trọng Phụng', FALSE, TRUE, 3, 2),

-- Kỹ năng & Tâm lý (Category 4, 7)
(6, 'Tâm lý học hành vi', 'behaviorwatson.jpg', 169000, 35, 'Phân tích về chủ nghĩa hành vi trong tâm lý học', '2021-11-11', '15.5x23.5cm', 'Khương Nguy', 'Mềm', 412, 'John B. Watson', FALSE, TRUE, 7, 5),
(11, 'Khéo ăn khéo nói sẽ có được thiên hạ', 'commskill.jpg', 110000, 55, 'Kỹ năng giao tiếp ứng xử thực tế', '2019-03-03', '13x20.5cm', 'Trác Nhã', 'Mềm', 280, 'Trác Nhã', FALSE, TRUE, 4, 5),
(15, 'Tư duy phản biện', 'criticalthinking.jpg', 145000, 40, 'Phương pháp rèn luyện tư duy sắc bén', '2019-09-09', '14.5x20.5cm', 'Zoe F', 'Mềm', 320, 'Tom Chatfield', FALSE, TRUE, 4, 5),
(25, 'Đắc Nhân Tâm', 'howtowin.jpg', 110000, 120, 'Cuốn sách nổi tiếng nhất về nghệ thuật giao tiếp', '2021-01-01', '14.5x20.5cm', 'Nguyễn Hiến Lê', 'Mềm', 320, 'Dale Carnegie', FALSE, TRUE, 4, 2),

-- Ngoại ngữ (Category 5)
(8, 'English Grammar in Use', 'grammarmurphy.jpg', 250000, 100, 'Sách học ngữ pháp tiếng Anh phổ biến nhất thế giới', '2019-08-01', '19x26cm', NULL, 'Mềm', 380, 'Raymond Murphy', FALSE, TRUE, 5, 2),

-- Khoa học (Category 6)
(16, 'Vật lý đại cương (Tập 1)', 'physicshalliday.jpg', 185000, 30, 'Cơ học và Nhiệt học cho sinh viên kỹ thuật', '2020-02-02', '19x27cm', 'Nhiều dịch giả', 'Mềm', 450, 'David Halliday', FALSE, TRUE, 6, 1),
(17, 'Hóa học đại cương', 'chemchang.jpg', 210000, 28, 'Nguyên lý hóa học hiện đại', '2021-06-06', '19x27cm', 'Nhóm dịch giả', 'Mềm', 600, 'Raymond Chang', FALSE, TRUE, 6, 1),

-- Lịch sử & Triết học (Category 9, 10)
(12, 'Lịch sử Việt Nam (Từ nguồn gốc đến thế kỷ XIX)', 'historyvn.jpg', 260000, 20, 'Công trình nghiên cứu lịch sử của giáo sư Phan Huy Lê', '2020-12-12', '16x24cm', NULL, 'Cứng', 850, 'Phan Huy Lê', FALSE, TRUE, 9, 1),
(7, 'Lịch sử triết học phương Tây', 'philorussell.jpg', 350000, 20, 'Toàn cảnh triết học từ cổ đại đến hiện đại', '2021-04-18', '16x24cm', 'Dương Ngọc Dũng', 'Cứng', 900, 'Bertrand Russell', FALSE, TRUE, 10, 4);

INSERT INTO orders (create_date, address, status, email) VALUES
('2026-01-20 10:00:00', 'Quận 12, TP.HCM', 2, 'Hoc@gmail.com'),
('2026-01-21 11:00:00', 'Quận 12, TP.HCM', 1, 'MinhHieu@gmail.com'),
('2026-01-22 12:00:00', 'Quận 12, TP.HCM', 0, 'TrungHieu@gmail.com'),
('2026-01-23 13:00:00', 'Quận 12, TP.HCM', 2, 'PhuongTram@gmail.com'),
('2026-01-24 14:00:00', 'Quận 12, TP.HCM', 3, 'ThuyVy@gmail.com'),
('2026-01-25 09:00:00', 'Quận 12, TP.HCM', 1, 'Hoc@gmail.com'),
('2026-01-26 10:30:00', 'Quận 12, TP.HCM', 2, 'MinhHieu@gmail.com'),
('2026-01-27 15:20:00', 'Quận 12, TP.HCM', 0, 'TrungHieu@gmail.com'),
('2026-01-28 16:40:00', 'Quận 12, TP.HCM', 1, 'PhuongTram@gmail.com'),
('2026-01-29 18:00:00', 'Quận 12, TP.HCM', 2, 'ThuyVy@gmail.com'),
('2026-01-30 08:15:00', 'Quận 12, TP.HCM', 0, 'Hoc@gmail.com'),
('2026-01-30 09:45:00', 'Quận 12, TP.HCM', 1, 'MinhHieu@gmail.com'),
('2026-01-31 11:30:00', 'Quận 12, TP.HCM', 2, 'TrungHieu@gmail.com'),
('2026-01-31 14:10:00', 'Quận 12, TP.HCM', 3, 'PhuongTram@gmail.com'),
('2026-01-31 16:55:00', 'Quận 12, TP.HCM', 2, 'ThuyVy@gmail.com'),
('2026-02-01 10:00:00', 'Quận 12, TP.HCM', 1, 'Hoc@gmail.com'),
('2026-02-01 12:20:00', 'Quận 12, TP.HCM', 0, 'MinhHieu@gmail.com'),
('2026-02-01 13:40:00', 'Quận 12, TP.HCM', 2, 'TrungHieu@gmail.com'),
('2026-02-01 15:00:00', 'Quận 12, TP.HCM', 1, 'PhuongTram@gmail.com'),
('2026-02-01 17:30:00', 'Quận 12, TP.HCM', 2, 'ThuyVy@gmail.com');


INSERT INTO order_details (price, quantity, book_id, order_id) VALUES
(120000, 1, 1, 1),
(180000, 1, 2, 1),

(150000, 2, 3, 2),
(170000, 1, 4, 2),

(80000, 1, 5, 3),
(160000, 1, 6, 3),

(190000, 1, 7, 4),
(210000, 1, 8, 4),

(220000, 1, 9, 5),
(200000, 1, 10, 5),

(110000, 2, 11, 6),
(175000, 1, 12, 6),

(165000, 1, 13, 7),
(230000, 1, 14, 7),

(130000, 2, 15, 8),
(190000, 1, 16, 8),

(160000, 1, 17, 9),
(145000, 1, 18, 9),

(155000, 2, 19, 10),
(240000, 1, 20, 10),

(120000, 1, 1, 11),
(180000, 1, 2, 11),

(150000, 1, 3, 12),
(170000, 1, 4, 12),

(80000, 2, 5, 13),
(160000, 1, 6, 13),

(190000, 1, 7, 14),
(210000, 1, 8, 14),

(220000, 1, 9, 15),
(200000, 1, 10, 15),

(110000, 1, 11, 16),
(175000, 1, 12, 16),

(165000, 2, 13, 17),
(230000, 1, 14, 17),

(130000, 1, 15, 18),
(190000, 1, 16, 18),

(160000, 1, 17, 19),
(145000, 2, 18, 19),

(155000, 1, 19, 20),
(240000, 1, 20, 20);

INSERT INTO cart (email) VALUES
('admin@fpt.edu.vn'),
('Hoc@gmail.com'),
('MinhHieu@gmail.com'),
('TrungHieu@gmail.com'),
('PhuongTram@gmail.com'),
('ThuyVy@gmail.com');


-- Bổ sung 60 bản ghi mới vào cart_details
INSERT INTO cart_details (cart_id, book_id, quantity, added_at) VALUES
-- Giỏ hàng Admin (ID: 1) - Tập trung các sách chuyên khảo cao cấp
(1, 7, 1, '2026-01-15 08:30:00'), (1, 12, 1, '2026-01-15 09:15:00'), (1, 16, 2, '2026-01-16 10:00:00'),
(1, 17, 2, '2026-01-16 10:05:00'), (1, 3, 1, '2026-01-17 14:20:00'), (1, 4, 1, '2026-01-17 14:22:00'),
(1, 18, 5, '2026-01-18 11:00:00'), (1, 22, 2, '2026-01-18 11:05:00'), (1, 24, 3, '2026-01-19 16:45:00'),
(1, 15, 1, '2026-01-20 09:30:00'),

-- Giỏ hàng User 1 (ID: 2) - Persona: Lập trình viên Fullstack
(2, 9, 1, '2026-01-10 20:15:00'), (2, 10, 1, '2026-01-10 20:17:00'), (2, 13, 1, '2026-01-11 07:30:00'),
(2, 14, 1, '2026-01-11 07:32:00'), (2, 19, 1, '2026-01-12 18:20:00'), (2, 20, 1, '2026-01-12 18:22:00'),
(2, 21, 1, '2026-01-13 12:00:00'), (2, 1, 1, '2026-01-14 15:10:00'), (2, 2, 1, '2026-01-14 15:12:00'),
(2, 8, 2, '2026-01-15 22:00:00'),

-- Giỏ hàng User 2 (ID: 3) - Persona: Sinh viên chuyên ngành Khoa học & Triết học
(3, 16, 3, '2026-01-14 09:00:00'), (3, 17, 3, '2026-01-14 09:05:00'), (3, 7, 1, '2026-01-15 13:45:00'),
(3, 12, 1, '2026-01-16 14:20:00'), (3, 6, 2, '2026-01-17 10:30:00'), (3, 15, 1, '2026-01-18 08:15:00'),
(3, 13, 1, '2026-01-19 19:00:00'), (3, 20, 1, '2026-01-20 21:00:00'), (3, 9, 1, '2026-01-20 21:05:00'),
(3, 10, 1, '2026-01-20 21:10:00'),

-- Giỏ hàng User 3 (ID: 4) - Persona: Quản lý dự án & Kỹ năng mềm
(4, 4, 2, '2026-01-12 11:30:00'), (4, 18, 3, '2026-01-12 11:35:00'), (4, 22, 1, '2026-01-13 16:20:00'),
(4, 25, 4, '2026-01-14 10:00:00'), (4, 11, 2, '2026-01-15 15:45:00'), (4, 15, 1, '2026-01-16 12:00:00'),
(4, 3, 1, '2026-01-17 09:30:00'), (4, 23, 1, '2026-01-18 14:15:00'), (4, 5, 2, '2026-01-19 17:50:00'),
(4, 1, 1, '2026-01-20 10:20:00'),

-- Giỏ hàng User 4 (ID: 5) - Persona: Phụ huynh & Độc giả văn học
(5, 5, 10, '2026-01-10 08:00:00'), (5, 24, 2, '2026-01-11 09:30:00'), (5, 23, 3, '2026-01-12 14:15:00'),
(5, 11, 1, '2026-01-13 11:00:00'), (5, 25, 1, '2026-01-14 16:45:00'), (5, 8, 1, '2026-01-15 19:20:00'),
(5, 22, 1, '2026-01-16 13:10:00'), (5, 6, 1, '2026-01-17 10:00:00'), (5, 12, 1, '2026-01-18 15:30:00'),
(5, 16, 1, '2026-01-19 09:15:00'),

-- Giỏ hàng User 5 (ID: 6) - Persona: Sinh viên CNTT năm cuối
(6, 14, 1, '2026-01-15 20:00:00'), (6, 13, 1, '2026-01-15 20:05:00'), (6, 20, 1, '2026-01-16 21:30:00'),
(6, 21, 1, '2026-01-17 14:00:00'), (6, 19, 2, '2026-01-17 14:10:00'), (6, 2, 1, '2026-01-18 11:45:00'),
(6, 10, 1, '2026-01-19 10:20:00'), (6, 9, 1, '2026-01-19 10:25:00'), (6, 1, 1, '2026-01-20 16:00:00'),
(6, 8, 1, '2026-01-20 16:15:00');



