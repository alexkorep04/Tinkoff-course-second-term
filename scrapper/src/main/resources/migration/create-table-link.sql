SET TIME ZONE 'UTC';

CREATE TABLE Link
(
    link_id BIGINT GENERATED ALWAYS AS IDENTITY,
    link_name varchar(511) NOT NULL,
    last_check TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_update TIMESTAMP,
    PRIMARY KEY (link_id)
);
