-- ==============================
-- User
-- ==============================
CREATE TABLE app_user (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          full_name VARCHAR(255),
                          email VARCHAR(255) UNIQUE,
                          password VARCHAR(255),
                          role VARCHAR(50) DEFAULT 'USER'
);

-- ==============================
-- Category
-- ==============================
CREATE TABLE category (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) UNIQUE NOT NULL
);

-- ==============================
-- Subcategory
-- ==============================
CREATE TABLE subcategory (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(255) UNIQUE NOT NULL,
                             category_id BIGINT NOT NULL,
                             CONSTRAINT fk_subcategory_category FOREIGN KEY (category_id) REFERENCES category(id)
);

-- ==============================
-- Location
-- ==============================
CREATE TABLE location (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) UNIQUE NOT NULL,
                          address VARCHAR(255)
);

-- ==============================
-- Item
-- ==============================
CREATE TABLE app_item (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255),
                          size VARCHAR(50),
                          description VARCHAR(1000),
                          brand VARCHAR(255),
                          location_id BIGINT NOT NULL,
                          gender VARCHAR(50),
                          category_id BIGINT NOT NULL,
                          subcategory_id BIGINT NOT NULL,
                          zustand VARCHAR(50),
                          CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES location(id),
                          CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(id),
                          CONSTRAINT fk_subcategory FOREIGN KEY (subcategory_id) REFERENCES subcategory(id)
);

-- ==============================
-- Rental
-- ==============================
CREATE TABLE app_rental (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            rental_date DATE,
                            end_date DATE,
                            return_date DATE,
                            extended BOOLEAN DEFAULT FALSE,
                            user_id BIGINT,
                            item_id BIGINT,
                            CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES app_user(id),
                            CONSTRAINT fk_item FOREIGN KEY (item_id) REFERENCES app_item(id)
);


-- ==============================
-- Kategorien und Subkategorien
-- ==============================
INSERT INTO category (name) VALUES
                                ('KLEIDUNG'),
                                ('EQUIPMENT'),
                                ('ACCESSOIRES');

INSERT INTO subcategory (name, category_id) VALUES
                                                ('JACKEN',      (SELECT id FROM category WHERE name = 'KLEIDUNG')),
                                                ('HOSEN',       (SELECT id FROM category WHERE name = 'KLEIDUNG')),
                                                ('SNOWBOARDS',  (SELECT id FROM category WHERE name = 'EQUIPMENT')),
                                                ('FLASCHEN',    (SELECT id FROM category WHERE name = 'EQUIPMENT')),
                                                ('HANDSCHUHE',  (SELECT id FROM category WHERE name = 'ACCESSOIRES'));

-- ==============================
-- Standorte
-- ==============================
INSERT INTO location (name, address) VALUES
                                         ('LOTHSTRASSE', 'Lothstraße 34'),
                                         ('KARLSTRASSE', 'Karlstraße 20'),
                                         ('PASING',      'Pasinger Str. 5');

-- ==============================
-- Beispiel-User
-- ==============================
INSERT INTO app_user (full_name, email, password, role) VALUES
                                                            ('Anna Admin', 'admin@hm.edu', 'admin123', 'ADMIN'),
                                                            ('Ben Benutzer', 'ben@hm.edu', 'benpass', 'USER');

-- ==============================
-- Beispiel-Items (mit FK-IDs)
-- ==============================
INSERT INTO app_item (name, size, description, brand, location_id, gender, category_id, subcategory_id, zustand)
VALUES
    ('Winterjacke', 'L', 'Warme Winterjacke für Damen', 'North Face',
     (SELECT id FROM location WHERE name = 'LOTHSTRASSE'),
     'DAMEN',
     (SELECT id FROM category WHERE name = 'KLEIDUNG'),
     (SELECT id FROM subcategory WHERE name = 'JACKEN'),
     'NEU'
    ),
    ('Skihose', 'M', 'Wasserdicht und bequem', 'Burton',
     (SELECT id FROM location WHERE name = 'LOTHSTRASSE'),
     'HERREN',
     (SELECT id FROM category WHERE name = 'KLEIDUNG'),
     (SELECT id FROM subcategory WHERE name = 'HOSEN'),
     'GEBRAUCHT'
    ),
    ('Snowboard', '120cm', 'Perfekt für Anfänger', 'Nitro',
     (SELECT id FROM location WHERE name = 'KARLSTRASSE'),
     'HERREN',
     (SELECT id FROM category WHERE name = 'EQUIPMENT'),
     (SELECT id FROM subcategory WHERE name = 'SNOWBOARDS'),
     'GEBRAUCHT'
    ),
    ('Flasche', '1.5L', 'BPA-frei', 'Nalgene',
     (SELECT id FROM location WHERE name = 'PASING'),
     'UNISEX',
     (SELECT id FROM category WHERE name = 'EQUIPMENT'),
     (SELECT id FROM subcategory WHERE name = 'FLASCHEN'),
     'NEU'
    ),
    ('Handschuhe', 'S', 'Winddicht', 'Reusch',
     (SELECT id FROM location WHERE name = 'LOTHSTRASSE'),
     'DAMEN',
     (SELECT id FROM category WHERE name = 'ACCESSOIRES'),
     (SELECT id FROM subcategory WHERE name = 'HANDSCHUHE'),
     'NEU'
    );

-- ==============================
-- Beispiel-Rental
-- ==============================
INSERT INTO app_rental (rental_date, end_date, return_date, extended, user_id, item_id)
VALUES (
                   CURRENT_DATE,
                   DATEADD('DAY', 30, CURRENT_DATE),
                   NULL,
                   FALSE,
                   (SELECT id FROM app_user WHERE email = 'ben@hm.edu'),
                   (SELECT id FROM app_item WHERE name = 'Snowboard')
       );
