-- V2__seed_master_data.sql
-- Seed master data for Attendance Management System

-- Insert Roles
INSERT INTO roles (id, name)
VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'EMPLOYEE'),
    ('550e8400-e29b-41d4-a716-446655440002', 'MANAGER')
ON CONFLICT (name) DO NOTHING;

-- Insert Departments
INSERT INTO departments (id, name)
VALUES
    ('550e8400-e29b-41d4-a716-446655440101', 'IT'),
    ('550e8400-e29b-41d4-a716-446655440102', 'HR'),
    ('550e8400-e29b-41d4-a716-446655440103', 'Finance'),
    ('550e8400-e29b-41d4-a716-446655440104', 'Operations')
ON CONFLICT (name) DO NOTHING;

-- Insert Test Users (Employees)
-- Password: Employee@123 (BCrypt encoded)
INSERT INTO users (id, name, email, password, employee_id, role_id, department_id, enabled)
VALUES
    ('550e8400-e29b-41d4-a716-446655440201', 'John Smith', 'john.smith@company.com', '$2a$10$SlVZQw8K4iHdPVdX1eOb9.YpqQcJYSN4YqhXJLCL6u1CPCv5KaOwa', 'EMP-001', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440101', true),
    ('550e8400-e29b-41d4-a716-446655440202', 'Jane Doe', 'jane.doe@company.com', '$2a$10$SlVZQw8K4iHdPVdX1eOb9.YpqQcJYSN4YqhXJLCL6u1CPCv5KaOwa', 'EMP-002', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440101', true),
    ('550e8400-e29b-41d4-a716-446655440203', 'Mike Johnson', 'mike.johnson@company.com', '$2a$10$SlVZQw8K4iHdPVdX1eOb9.YpqQcJYSN4YqhXJLCL6u1CPCv5KaOwa', 'EMP-003', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440102', true),
    ('550e8400-e29b-41d4-a716-446655440204', 'Sarah Williams', 'sarah.williams@company.com', '$2a$10$SlVZQw8K4iHdPVdX1eOb9.YpqQcJYSN4YqhXJLCL6u1CPCv5KaOwa', 'EMP-004', '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440103', true)
ON CONFLICT (email) DO NOTHING;

-- Insert Test Manager
INSERT INTO users (id, name, email, password, employee_id, role_id, department_id, enabled)
VALUES
    ('550e8400-e29b-41d4-a716-446655440301', 'Alex Manager', 'alex.manager@company.com', '$2a$10$SlVZQw8K4iHdPVdX1eOb9.YpqQcJYSN4YqhXJLCL6u1CPCv5KaOwa', 'MGR-001', '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440101', true)
ON CONFLICT (email) DO NOTHING;


