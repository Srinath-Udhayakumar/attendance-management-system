-- V5__remove_pending_migration.sql
-- Remove pending migration from flyway history

DELETE FROM flyway_schema_history WHERE version = '5';
