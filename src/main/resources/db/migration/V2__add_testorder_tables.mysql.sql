drop table if exists `test_order`;
drop table if exists `test_order_line`;

CREATE TABLE `test_order` (
    id            varchar(36)     NOT NULL,
    created_at    timestamp(6)    DEFAULT NULL,
    updated_at    timestamp(6)    DEFAULT NULL,
    patient_ref   varchar(255)    DEFAULT NULL,
    version       bigint          DEFAULT NULL,
    patient_id    varchar(36)     DEFAULT NULL,
    PRIMARY KEY (id),
    KEY patient_id (patient_id),
    CONSTRAINT "test_order_ibfk_1" FOREIGN KEY (patient_id) REFERENCES patient (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `test_order_line` (
    id            varchar(36)     NOT NULL,
    sample_id     varchar(36)     DEFAULT NULL,
    created_at    timestamp(6)    DEFAULT NULL,
    updated_at    timestamp(6)    DEFAULT NULL,
    version       bigint          DEFAULT NULL,
    test_order_id varchar(36)     DEFAULT NULL,
    test_type     smallint        NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT "test_order_line_ibfk_1" FOREIGN KEY (test_order_id) REFERENCES test_order (id),
    CONSTRAINT "test_order_line_ibfk_1" FOREIGN KEY (sample_id) REFERENCES sample (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;