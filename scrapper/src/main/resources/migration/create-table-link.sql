SET TIME ZONE 'UTC';

CREATE TABLE link
(
    link_id BIGINT GENERATED ALWAYS AS IDENTITY,
    link_name varchar(511),
    last_check TIMESTAMP,
    last_update TIMESTAMP,
    last_commit TIMESTAMP,
    amount_issues INT DEFAULT -1,
    type varchar(31),
    PRIMARY KEY (link_id)
);
