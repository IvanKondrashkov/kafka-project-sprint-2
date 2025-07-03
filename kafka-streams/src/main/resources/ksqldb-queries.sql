CREATE STREAM messages_stream (
  user_id       VARCHAR,
  recipient_id  VARCHAR,
  message       VARCHAR,
  "timestamp"   TIMESTAMP
) WITH (
    KAFKA_TOPIC='messages',
    VALUE_FORMAT='JSON',
    PARTITIONS=3
);

CREATE TABLE send_message_total AS
SELECT
    ms.user_id,
    COUNT(*) AS message_ctn
FROM messages_stream ms
GROUP BY ms.user_id
EMIT CHANGES;

CREATE TABLE send_recipient_message_uniq WITH (KEY_FORMAT='JSON') AS
SELECT
    ms.user_id,
    ms.recipient_id,
    COUNT(*) AS message_uniq_ctn
FROM messages_stream ms
GROUP BY ms.user_id, ms.recipient_id
EMIT CHANGES;

CREATE TABLE recipient_message_uniq AS
SELECT
    srmu.user_id,
    COUNT(*) AS message_uniq_ctn
FROM send_recipient_message_uniq srmu
GROUP BY srmu.user_id
EMIT CHANGES;

CREATE TABLE top_users AS
SELECT
    ms.user_id,
    TOPK(ms.user_id, 5) AS message_top
FROM messages_stream ms
GROUP BY ms.user_id
EMIT CHANGES;

CREATE TABLE user_statistics AS
SELECT
    smt.user_id,
    smt.message_ctn,
    rmu.message_uniq_ctn
FROM send_message_total smt
LEFT OUTER JOIN recipient_message_uniq rmu ON smt.user_id = rmu.user_id
EMIT CHANGES;