-- ----------------------------
-- Records of gift_certificate
-- ----------------------------
BEGIN;
INSERT INTO gift_certificate (description, price, duration, create_date, last_update_date, name) VALUES ('Family certificate for New Year weekend travel', 41.00, 33, '2020-12-06 14:29:04', '2020-12-09 14:11:08', 'New Year certificate');
INSERT INTO gift_certificate (description, price, duration, create_date, last_update_date, name) VALUES ('Certificate for the 1st online purchase', 155.00, 90, '2022-01-06 14:29:04', '2022-03-06 10:11:01', 'Evroopt delivery certificate');
INSERT INTO gift_certificate (description, price, duration, create_date, last_update_date, name) VALUES ('Certificate can be applied only for personal training', 41.00, 365, '2021-01-01 20:48:32.999145', NULL, 'Gym Minsk certificate');
INSERT INTO gift_certificate (description, price, duration, create_date, last_update_date, name) VALUES ('Certificate can be used only in the summer sale', 200.00, 31, '2021-06-01 00:49:01.703544', NULL, 'Aliexpress summer certificate');
COMMIT;

-- ----------------------------
-- Records of tag
-- ----------------------------
BEGIN;
INSERT INTO tag (name) VALUES ('holiday');
INSERT INTO tag (name) VALUES ('purchase');
INSERT INTO tag (name) VALUES ('sport');
INSERT INTO tag (name) VALUES ('sale');
INSERT INTO tag (name) VALUES ('online');
INSERT INTO tag (name) VALUES ('discount');
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
COMMIT;