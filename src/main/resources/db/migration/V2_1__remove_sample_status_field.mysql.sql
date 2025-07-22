ALTER TABLE "sample" DROP COLUMN "status";
ALTER TABLE "sample" CHANGE COLUMN "type" "test_type" SMALLINT NOT NULL;