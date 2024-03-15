CREATE TABLE chat_link
(
    chat_id BIGINT REFERENCES Chat(id),
    link_id BIGINT REFERENCES Link(link_id),
    PRIMARY KEY(chat_id, link_id)
);

