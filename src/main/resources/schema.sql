-- Schema initialization for dibya002
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    mobile_number VARCHAR(20) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    pin_code VARCHAR(10) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    role VARCHAR(50) DEFAULT 'ROLE_USER',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS admins (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    admin_id VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    mobile_number VARCHAR(20),
    password_hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    role VARCHAR(50) DEFAULT 'ROLE_ADMIN',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS password_reset (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_type VARCHAR(20) NOT NULL,
    identifier VARCHAR(100) NOT NULL,
    reset_token VARCHAR(100) NOT NULL,
    expiry_time DATETIME NOT NULL,
    is_used BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    address_line TEXT NOT NULL,
    at_locality VARCHAR(150),
    post_office VARCHAR(100),
    district VARCHAR(100),
    state VARCHAR(100),
    pin_code VARCHAR(10) NOT NULL,
    nearby_place VARCHAR(150),
    is_default BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS diet_master (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    diet_id VARCHAR(50) UNIQUE NOT NULL,
    diet_date DATE NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    tips TEXT,
    instructions TEXT,
    image_url VARCHAR(500),
    video_url VARCHAR(500),
    other_instructions TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_by VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS diet_user_upload (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    user_name VARCHAR(150),
    diet_id VARCHAR(50),
    diet_title VARCHAR(200),
    upload_date DATE NOT NULL,
    file_path VARCHAR(500),
    notes TEXT,
    upload_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS fitness_master (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fitness_id VARCHAR(50) UNIQUE NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    instructions TEXT,
    image_url VARCHAR(500),
    video_url VARCHAR(500),
    duration VARCHAR(50),
    repetitions VARCHAR(50),
    other_instructions TEXT,
    category VARCHAR(50) DEFAULT 'General',
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS fitness_completion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    user_name VARCHAR(150),
    fitness_id VARCHAR(50) NOT NULL,
    fitness_title VARCHAR(200),
    completion_date DATE NOT NULL,
    completion_time TIME NOT NULL,
    completion_status VARCHAR(50) DEFAULT 'COMPLETED',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_fitness_date (user_id, fitness_id, completion_date)
);

CREATE TABLE IF NOT EXISTS product_master (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    short_description VARCHAR(500),
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    image_url VARCHAR(500),
    why_to_use TEXT,
    who_can_use TEXT,
    how_to_use TEXT,
    other_instructions TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) UNIQUE NOT NULL,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id VARCHAR(50) NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    product_price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(500),
    quantity INT NOT NULL DEFAULT 1,
    is_selected BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(50) UNIQUE NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    user_name VARCHAR(150),
    user_mobile VARCHAR(20),
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    grand_total DECIMAL(10,2) NOT NULL,
    order_status VARCHAR(50) NOT NULL DEFAULT 'Order Placed',
    payment_status VARCHAR(50) NOT NULL DEFAULT 'Pending',
    payment_updated_at DATETIME,
    delivered_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_db_id BIGINT NOT NULL,
    product_id VARCHAR(50) NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    product_image VARCHAR(500),
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    product_total DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS order_addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_db_id BIGINT NOT NULL,
    customer_name VARCHAR(150) NOT NULL,
    mobile_number VARCHAR(20) NOT NULL,
    address_line TEXT NOT NULL,
    at_locality VARCHAR(150),
    post_office VARCHAR(100),
    district VARCHAR(100),
    state VARCHAR(100),
    pin_code VARCHAR(10) NOT NULL,
    nearby_place VARCHAR(150)
);

CREATE TABLE IF NOT EXISTS meeting_master (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    meeting_id VARCHAR(50) UNIQUE NOT NULL,
    topic VARCHAR(250) NOT NULL,
    host_details VARCHAR(200) NOT NULL,
    agenda TEXT,
    meeting_date DATE NOT NULL,
    start_time TIME NOT NULL,
    duration VARCHAR(50),
    meeting_mode VARCHAR(50) NOT NULL,
    meeting_address TEXT,
    meeting_link VARCHAR(500),
    other_details TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    is_cancelled BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS organization_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    org_name VARCHAR(200) NOT NULL,
    logo_url VARCHAR(500),
    about_info TEXT,
    address TEXT,
    mobile_number VARCHAR(20),
    email VARCHAR(100),
    website VARCHAR(150),
    other_info TEXT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
