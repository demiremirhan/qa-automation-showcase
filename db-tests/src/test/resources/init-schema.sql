CREATE TABLE IF NOT EXISTS products (
                                        id          SERIAL PRIMARY KEY,
                                        title       VARCHAR(255) NOT NULL,
    category    VARCHAR(100) NOT NULL,
    price       NUMERIC(10,2) NOT NULL CHECK (price >= 0),
    stock       INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    rating      NUMERIC(3,2) CHECK (rating BETWEEN 0 AND 5),
    sku         VARCHAR(50) UNIQUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS customers (
                                         id          SERIAL PRIMARY KEY,
                                         first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    email       VARCHAR(255) UNIQUE NOT NULL,
    city        VARCHAR(100),
    active      BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS orders (
                                      id            SERIAL PRIMARY KEY,
                                      customer_id   INT NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    product_id    INT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    quantity      INT NOT NULL CHECK (quantity > 0),
    total_price   NUMERIC(10,2) NOT NULL,
    status        VARCHAR(30) NOT NULL DEFAULT 'PENDING'
    CHECK (status IN ('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED')),
    ordered_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_orders_status   ON orders(status);
CREATE INDEX idx_products_category ON products(category);