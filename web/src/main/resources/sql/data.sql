-- ----------------------------
-- Records of gift_certificate
-- ----------------------------
BEGIN;
INSERT INTO gift_certificates (description, price, duration, create_date, last_update_date, name, operation, timestamp) VALUES ('Family certificate for New Year weekend travel', 41.00, 33, '2020-12-06 14:29:04', '2020-12-09 14:11:08', 'New Year certificate', 'INSERT', 1650528752084);
INSERT INTO gift_certificates (description, price, duration, create_date, last_update_date, name, operation, timestamp) VALUES ('Certificate for the 1st online purchase', 155.00, 90, '2022-01-06 14:29:04', '2022-03-06 10:11:01', 'Evroopt delivery certificate', 'INSERT', 1650528752084);
INSERT INTO gift_certificates (description, price, duration, create_date, last_update_date, name, operation, timestamp) VALUES ('Certificate can be applied only for personal training', 41.00, 365, '2021-01-01 20:48:32.999145', NULL, 'Gym Minsk certificate', 'INSERT', 1650528752084);
INSERT INTO gift_certificates (description, price, duration, create_date, last_update_date, name, operation, timestamp) VALUES ('Certificate can be used only in the summer sale', 200.00, 31, '2021-06-01 00:49:01.703544', NULL, 'Aliexpress summer certificate', 'INSERT', 1650528752084);
INSERT INTO gift_certificates (description, price, duration, create_date, last_update_date, name, operation, timestamp) VALUES ('Test cert descr', 200.00, 31, '2021-06-01 00:49:01.703544', NULL, 'Test cert', 'INSERT', 1650528752084);

COMMIT;

-- ----------------------------
-- Records of tag
-- ----------------------------
BEGIN;
INSERT INTO tags (name, operation, timestamp) VALUES ('holiday', 'INSERT', 1650528752084);
INSERT INTO tags (name, operation, timestamp) VALUES ('purchase', 'INSERT', 1650528752084);
INSERT INTO tags (name, operation, timestamp) VALUES ('sport', 'INSERT', 1650528752084);
INSERT INTO tags (name, operation, timestamp) VALUES ('sale', 'INSERT', 1650528752084);
INSERT INTO tags (name, operation, timestamp) VALUES ('online', 'INSERT', 1650528752084);
INSERT INTO tags (name, operation, timestamp) VALUES ('discount', 'INSERT', 1650528752084);
INSERT INTO tags (name, operation, timestamp) VALUES ('test', 'INSERT', 1650528752084);
COMMIT;

-- ----------------------------
-- Records of gift_certificate_tag
-- ----------------------------
BEGIN;
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (1, 1);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (1, 2);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (1, 3);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (2, 1);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (2, 4);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (2, 5);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (2, 6);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (1, 5);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (3, 5);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (4, 7);
COMMIT;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO users (operation, timestamp, birthday, first_name, last_name, login, password) VALUES ('INSERT', 1650528752084, '2022-04-21', 'John', 'Doe', 'qwerty', '123');
INSERT INTO users (operation, timestamp, birthday, first_name, last_name, login, password) VALUES ('INSERT', 1650528752084, '2022-04-21', 'Ivan', 'Petrov', 'ivan', '321');
COMMIT;

-- ----------------------------
-- Records of orders
-- ----------------------------
BEGIN;
INSERT INTO orders (operation, timestamp, purchase_timestamp, user_id, cost) VALUES ('INSERT', 1650705552456, 1650705552455, 1, '0.14');
INSERT INTO orders (operation, timestamp, purchase_timestamp, user_id, cost) VALUES ('INSERT', 1650705552456, 1650705552455, 1, '1.11');
INSERT INTO orders (operation, timestamp, purchase_timestamp, user_id, cost) VALUES ('INSERT', 1650705552456, 1650705552455, 1, '34.97');
INSERT INTO orders (operation, timestamp, purchase_timestamp, user_id, cost) VALUES ('INSERT', 1650705552456, 1650705552455, 2, '100.00');
COMMIT;

-- ----------------------------
-- Records of order_certificate
-- ----------------------------
BEGIN;
INSERT INTO order_certificate (order_id, gift_certificate_id) VALUES (1, 1);
INSERT INTO order_certificate (order_id, gift_certificate_id) VALUES (1, 2);
INSERT INTO order_certificate (order_id, gift_certificate_id) VALUES (2, 2);
INSERT INTO order_certificate (order_id, gift_certificate_id) VALUES (2, 3);
INSERT INTO order_certificate (order_id, gift_certificate_id) VALUES (3, 4);
INSERT INTO order_certificate (order_id, gift_certificate_id) VALUES (3, 1);
INSERT INTO order_certificate (order_id, gift_certificate_id) VALUES (4, 3);
INSERT INTO order_certificate (order_id, gift_certificate_id) VALUES (4, 2);
INSERT INTO order_certificate (order_id, gift_certificate_id) VALUES (4, 2);
INSERT INTO order_certificate (order_id, gift_certificate_id) VALUES (4, 4);
INSERT INTO order_certificate (order_id, gift_certificate_id) VALUES (4, 1);
COMMIT;