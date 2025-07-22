CREATE TABLE "test_order" (
    "id" varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
    "created_at" timestamp(6) DEFAULT NULL,
    "updated_at" timestamp(6) DEFAULT NULL,
    "patient_ref" varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    "version" bigint DEFAULT NULL,
    "patient_id" varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY ("id"),
    KEY "patient_id" ("patient_id"),
    CONSTRAINT "test_order_ibfk_1" FOREIGN KEY ("patient_id") REFERENCES "patient" ("id")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;