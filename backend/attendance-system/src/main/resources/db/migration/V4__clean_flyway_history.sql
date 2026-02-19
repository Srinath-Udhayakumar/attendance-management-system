-- V4__clean_flyway_history.sql
-- Clean up the pending migration from flyway history

-- Remove the pending migration that doesn't exist anymore
DELETE FROM flyway_schema_history WHERE version = '4';
