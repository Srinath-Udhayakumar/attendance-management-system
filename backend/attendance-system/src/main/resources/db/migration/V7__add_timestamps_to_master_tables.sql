-- V7__add_timestamps_to_master_tables.sql
-- Add created_at and updated_at columns to roles and departments tables

-- Add timestamp columns to roles table if they don't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE column_name = 'created_at' 
        AND table_name = 'roles' 
        AND table_schema = 'public'
    ) THEN
        ALTER TABLE roles ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
        ALTER TABLE roles ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
    END IF;
END $$;

-- Add timestamp columns to departments table if they don't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE column_name = 'created_at' 
        AND table_name = 'departments' 
        AND table_schema = 'public'
    ) THEN
        ALTER TABLE departments ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
        ALTER TABLE departments ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
    END IF;
END $$;
