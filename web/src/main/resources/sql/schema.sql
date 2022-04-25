-- ----------------------------
-- Table structure for gift_certificate_tag
-- ----------------------------
DROP TABLE IF EXISTS gift_certificate_tag;
CREATE TABLE gift_certificate_tag (
                                      gift_certificate_id int4 NOT NULL,
                                      tag_id int4 NOT NULL
)
;

-- ----------------------------
-- Table structure for gift_certificates
-- ----------------------------
DROP TABLE IF EXISTS gift_certificates;
CREATE TABLE gift_certificates (
                                   id int4 NOT NULL auto_increment,
                                   description varchar(255),
                                   price numeric(10,2) NOT NULL,
                                   duration int2 NOT NULL,
                                   create_date timestamp(6) NOT NULL,
                                   last_update_date timestamp(6),
                                   name varchar(80) NOT NULL,
                                   operation varchar(255) NOT NULL,
                                   timestamp int8 NOT NULL
)
;

-- ----------------------------
-- Table structure for order_certificate
-- ----------------------------
DROP TABLE IF EXISTS order_certificate;
CREATE TABLE order_certificate (
                                   order_id int8 NOT NULL,
                                   gift_certificate_id int8 NOT NULL
)
;
-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
                        id int4 NOT NULL auto_increment,
                        operation varchar(255) NOT NULL,
                        timestamp int8 NOT NULL,
                        purchase_timestamp int8 NOT NULL,
                        user_id int8 NOT NULL,
                        cost numeric(19,2) NOT NULL
)
;

-- ----------------------------
-- Table structure for tags
-- ----------------------------
DROP TABLE IF EXISTS tags;
CREATE TABLE tags (
                      id int4 NOT NULL auto_increment,
                      name varchar(20) NOT NULL,
                      operation varchar(255) NOT NULL,
                      timestamp int8 NOT NULL
)
;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS users;
CREATE TABLE users (
                       id int4 NOT NULL auto_increment,
                       operation varchar(255) NOT NULL,
                       timestamp int8 NOT NULL,
                       birthday date,
                       first_name varchar(255) NOT NULL,
                       last_name varchar(255) NOT NULL,
                       login varchar(255) NOT NULL,
                       password varchar(255) NOT NULL
)
;


-- ----------------------------
-- Uniques structure for table gift_certificate_tag
-- ----------------------------
ALTER TABLE gift_certificate_tag ADD CONSTRAINT gift_certificate_tag_gift_certificate_id_tag_id_key UNIQUE (gift_certificate_id, tag_id);


-- ----------------------------
-- Primary Key structure for table gift_certificates
-- ----------------------------
ALTER TABLE gift_certificates ADD CONSTRAINT gift_certificate_pkey PRIMARY KEY (id);

-- ----------------------------
-- Primary Key structure for table orders
-- ----------------------------
ALTER TABLE orders ADD CONSTRAINT orders_pkey PRIMARY KEY (id);

-- -----
-- ----------------------------
-- Uniques structure for table tags
-- ----------------------------
ALTER TABLE tags ADD CONSTRAINT tag_name_key UNIQUE (name);

-- ----------------------------
-- Primary Key structure for table tags
-- ----------------------------
ALTER TABLE tags ADD CONSTRAINT tag_pkey PRIMARY KEY (id);

-- ----------------------------
-- Uniques structure for table users
-- ----------------------------
ALTER TABLE users ADD CONSTRAINT uk_ow0gan20590jrb00upg3va2fn UNIQUE (login);

-- ----------------------------
-- Primary Key structure for table users
-- ----------------------------
ALTER TABLE users ADD CONSTRAINT users_pkey PRIMARY KEY (id);

-- ----------------------------
-- Foreign Keys structure for table gift_certificate_tag
-- ----------------------------

ALTER TABLE gift_certificate_tag ADD CONSTRAINT gift_certificate_tag_gift_certificate_id_fkey FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificates (id) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE gift_certificate_tag ADD CONSTRAINT gift_certificate_tag_tag_id_fkey FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table order_certificate
-- ----------------------------
ALTER TABLE order_certificate ADD CONSTRAINT fkfuy07kfeh0fi1wxgsvo1mocab FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificates (id) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE order_certificate ADD CONSTRAINT fkrj5ul3jy6cil5g79jr6osfx5e FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table orders
-- ----------------------------
ALTER TABLE orders ADD CONSTRAINT fk32ql8ubntj5uh44ph9659tiih FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE NO ACTION;
