USE spkt_connect;

-- Xóa 2 cột này trước
--ALTER TABLE Reports DROP COLUMN reported_content_type;
--ALTER TABLE Reports DROP COLUMN reported_content_id;

ALTER TABLE Reports
ADD COLUMN reported_post_id INT NULL,
ADD COLUMN reported_comment_id INT NULL,
ADD COLUMN reported_product_id INT NULL;

ALTER TABLE Reports
ADD CONSTRAINT fk_reports_post
    FOREIGN KEY (reported_post_id) REFERENCES Posts(post_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
ADD CONSTRAINT fk_reports_comment
    FOREIGN KEY (reported_comment_id) REFERENCES Comments(comment_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
ADD CONSTRAINT fk_reports_product
    FOREIGN KEY (reported_product_id) REFERENCES Products(product_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;
    
USE spkt_connect;
ALTER TABLE reports
ADD COLUMN reported_share_id INT NULL,
ADD CONSTRAINT fk_reports_share
    FOREIGN KEY (reported_share_id) REFERENCES post_shares(share_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

