-- V6__add_master_data.sql
-- Add master data for Attendance Management System

-- Insert Roles
INSERT INTO roles (id, name)
VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'EMPLOYEE'),
    ('550e8400-e29b-41d4-a716-446655440002', 'MANAGER')
ON CONFLICT DO NOTHING;

-- Insert Departments  
INSERT INTO departments (id, name)
VALUES
    ('550e8400-e29b-41d4-a716-446655440101', 'IT'),
    ('550e8400-e29b-41d4-a716-446655440102', 'HR'),
    ('550e8400-e29b-41d4-a716-446655440103', 'Finance'),
    ('550e8400-e29b-41d4-a716-446655440104', 'Operations')
ON CONFLICT DO NOTHING;
