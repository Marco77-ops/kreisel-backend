-- data.sql
-- 👤 Beispiel-User (OHNE id - lass AUTO_INCREMENT arbeiten!)
INSERT INTO app_user (full_name, email, password, role) VALUES
                                                            ('Anna Admin', 'admin@hm.edu', 'admin123', 'ADMIN'),
                                                            ('Ben Benutzer', 'ben@hm.edu', 'benpass', 'USER');

-- 🎽 Beispiel-Items (OHNE id - lass AUTO_INCREMENT arbeiten!)
INSERT INTO app_item (name, size, available, description, brand, location, gender, category, subcategory, zustand)
VALUES
    ('Winterjacke', 'L', TRUE, 'Warme Winterjacke für Damen', 'North Face', 'LOTHSTRASSE', 'DAMEN', 'KLEIDUNG', 'JACKEN', 'NEU'),
    ('Skihose', 'M', TRUE, 'Wasserdicht und bequem', 'Burton', 'LOTHSTRASSE', 'HERREN', 'KLEIDUNG', 'HOSEN', 'GEBRAUCHT'),
    ('Snowboard', '120cm', FALSE, 'Perfekt für Anfänger', 'Nitro', 'KARLSTRASSE', 'HERREN', 'EQUIPMENT', 'SNOWBOARDS', 'GEBRAUCHT'),
    ('Flasche', '1.5L', TRUE, 'BPA-frei', 'Nalgene', 'PASING', 'UNISEX', 'EQUIPMENT', 'FLASCHEN', 'NEU'),
    ('Handschuhe', 'S', TRUE, 'Winddicht', 'Reusch', 'LOTHSTRASSE', 'DAMEN', 'ACCESSOIRES', 'HANDSCHUHE', 'NEU');

-- 📦 Beispiel-Rental (OHNE id - lass AUTO_INCREMENT arbeiten!)
-- Da wir nicht wissen welche IDs die User/Items bekommen haben, verwenden wir Subqueries
INSERT INTO app_rental (rental_date, end_date, return_date, extended, user_id, item_id)
VALUES (
                   CURRENT_DATE,
                   DATEADD('DAY', 30, CURRENT_DATE),
                   NULL,
                   FALSE,
                   (SELECT id FROM app_user WHERE email = 'ben@hm.edu'),
                   (SELECT id FROM app_item WHERE name = 'Snowboard')
       );
