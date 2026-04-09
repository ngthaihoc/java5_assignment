CREATE DATABASE IF NOT EXISTS java5_assignment;
USE java5_assignment;

-- Bảng accounts
CREATE TABLE IF NOT EXISTS accounts (
    email VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    fullname VARCHAR(100) NOT NULL,
    avatar VARCHAR(255),
    admin BOOLEAN DEFAULT FALSE,
    enabled BOOLEAN DEFAULT TRUE
);

-- Bảng categories 
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);

-- Bảng publishers
CREATE TABLE IF NOT EXISTS publishers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    contact_email VARCHAR(255)
);

-- Bảng books
CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    author_name VARCHAR(255),
    price DECIMAL(10, 2) NOT NULL,
    quantity INT,
    description TEXT,
    publish_date DATE,
    dimensions VARCHAR(255),
    translator VARCHAR(255),
    cover_type VARCHAR(255),
    page_count INT,
    available BOOLEAN DEFAULT TRUE,
    category_id BIGINT,
    publisher_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    FOREIGN KEY (publisher_id) REFERENCES publishers(id) ON DELETE SET NULL
);

-- Bảng cart
CREATE TABLE IF NOT EXISTS cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255),
    FOREIGN KEY (email) REFERENCES accounts(email) ON DELETE CASCADE
);

-- Bảng cart_details
CREATE TABLE IF NOT EXISTS cart_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantity INT,
    added_at DATETIME,
    cart_id BIGINT,
    book_id BIGINT,
    FOREIGN KEY (cart_id) REFERENCES cart(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- Bảng orders
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    create_date DATETIME,
    address VARCHAR(255),
    phone VARCHAR(255),
    status INT,
    email VARCHAR(255),
    FOREIGN KEY (email) REFERENCES accounts(email) ON DELETE CASCADE
);

-- Bảng order_details
CREATE TABLE IF NOT EXISTS order_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    price DECIMAL(10, 2),
    quantity INT,
    book_id BIGINT,
    order_id BIGINT,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);
