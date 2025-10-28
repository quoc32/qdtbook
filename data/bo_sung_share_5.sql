USE spkt_connect;
ALTER TABLE reports
ADD COLUMN reported_share_id INT NULL,
ADD CONSTRAINT fk_reports_share
    FOREIGN KEY (reported_share_id) REFERENCES post_shares(share_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;