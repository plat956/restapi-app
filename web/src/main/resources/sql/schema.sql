-- ----------------------------
-- Table structure for gift_certificate
-- ----------------------------
DROP TABLE IF EXISTS gift_certificate;
CREATE TABLE gift_certificate (
        id bigint auto_increment,
        description varchar(255),
        price numeric(10,2) NOT NULL,
        duration int2 NOT NULL,
        create_date timestamp(6) NOT NULL,
        last_update_date timestamp(6),
        name varchar(80) NOT NULL
);


-- ----------------------------
-- Table structure for gift_certificate_tag
-- ----------------------------
DROP TABLE IF EXISTS gift_certificate_tag;
CREATE TABLE gift_certificate_tag (
        gift_certificate_id int4 NOT NULL,
        tag_id int4 NOT NULL
);


-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS tag;
CREATE TABLE tag (
       id int4 NOT NULL auto_increment,
       name varchar(20) NOT NULL
);

-- ----------------------------
-- Primary Key structure for table gift_certificate
-- ----------------------------
ALTER TABLE gift_certificate ADD CONSTRAINT gift_certificate_pkey PRIMARY KEY (id);
-- ----------------------------
-- Uniques structure for table gift_certificate_tag
-- ----------------------------
ALTER TABLE gift_certificate_tag ADD CONSTRAINT gift_certificate_tag_gift_certificate_id_tag_id_key UNIQUE (gift_certificate_id, tag_id);

-- ----------------------------
-- Uniques structure for table tag
-- ----------------------------
ALTER TABLE tag ADD CONSTRAINT tag_name_key UNIQUE (name);

-- ----------------------------
-- Primary Key structure for table tag
-- ----------------------------
ALTER TABLE tag ADD CONSTRAINT tag_pkey PRIMARY KEY (id);

-- ----------------------------
-- Foreign Keys structure for table gift_certificate_tag
-- ----------------------------
ALTER TABLE gift_certificate_tag ADD CONSTRAINT gift_certificate_tag_gift_certificate_id_fkey FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate (id) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE gift_certificate_tag ADD CONSTRAINT gift_certificate_tag_tag_id_fkey FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE CASCADE ON UPDATE NO ACTION;
