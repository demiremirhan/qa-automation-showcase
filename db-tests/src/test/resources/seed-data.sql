INSERT INTO products (title, category, price, stock, rating, sku) VALUES
                                                                      ('Essence Mascara',         'beauty',      9.99,  100, 4.94, 'RCH-001'),
                                                                      ('Eyeshadow Palette',       'beauty',     19.99,   50, 3.28, 'RCH-002'),
                                                                      ('Powder Canister',         'beauty',     14.99,   75, 3.82, 'RCH-003'),
                                                                      ('Calvin Klein Eternity',   'fragrances', 89.99,   30, 4.08, 'FRG-001'),
                                                                      ('Chanel Coco Noir',        'fragrances',129.99,   15, 4.65, 'FRG-002'),
                                                                      ('Kitchen Knife Set',       'home',       49.99,  200, 4.70, 'HME-001'),
                                                                      ('HP Pavilion Laptop',      'laptops',   799.99,   10, 4.43, 'LPT-001'),
                                                                      ('Dell XPS 15',             'laptops',  1199.99,    5, 4.80, 'LPT-002'),
                                                                      ('iPhone 15',               'smartphones',999.99,   25, 4.60, 'PHN-001'),
                                                                      ('Samsung Galaxy S24',      'smartphones',899.99,   40, 4.55, 'PHN-002');

INSERT INTO customers (first_name, last_name, email, city) VALUES
                                                               ('Emirhan', 'Test',    'emirhan@test.com',  'Istanbul'),
                                                               ('Anna',   'Schmidt', 'anna@test.com',      'Berlin'),
                                                               ('John',   'Doe',     'john@test.com',      'New York'),
                                                               ('Yuki',   'Tanaka',  'yuki@test.com',      'Tokyo'),
                                                               ('Carlos', 'Rivera',  'carlos@test.com',    'Madrid');

INSERT INTO orders (customer_id, product_id, quantity, total_price, status) VALUES
                                                                                (1, 7, 1, 799.99,  'CONFIRMED'),
                                                                                (1, 1, 3,  29.97,  'DELIVERED'),
                                                                                (2, 5, 1, 129.99,  'SHIPPED'),
                                                                                (3, 9, 2, 1999.98, 'PENDING'),
                                                                                (4, 6, 1,  49.99,  'DELIVERED'),
                                                                                (5, 8, 1, 1199.99, 'CANCELLED');