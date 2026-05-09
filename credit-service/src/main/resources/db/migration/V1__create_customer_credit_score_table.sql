CREATE TABLE IF NOT EXISTS customer_credit_score (
    id              BIGINT      NOT NULL AUTO_INCREMENT,
    customer_id     BIGINT      NOT NULL,
    credit_score    INT         NOT NULL,
    created_at      DATETIME(6),
    updated_at      DATETIME(6),
    CONSTRAINT pk_customer_credit_score PRIMARY KEY (id),
    CONSTRAINT uq_customer_credit_score_customer_id UNIQUE (customer_id)
);
