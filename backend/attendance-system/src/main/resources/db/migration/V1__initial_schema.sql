-- V1__initial_schema.sql
-- Initial database schema for Attendance Management System

-- Roles table
CREATE TABLE IF NOT EXISTS roles (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_role_name UNIQUE (name)
);

CREATE INDEX IF NOT EXISTS idx_role_name ON roles(name);

-- Departments table
CREATE TABLE IF NOT EXISTS departments (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_department_name UNIQUE (name)
);

CREATE INDEX IF NOT EXISTS idx_department_name ON departments(name);

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    employee_id VARCHAR(20) NOT NULL UNIQUE,
    role_id UUID NOT NULL,
    department_id UUID NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT fk_user_department FOREIGN KEY (department_id) REFERENCES departments(id),
    CONSTRAINT uk_user_email UNIQUE (email),
    CONSTRAINT uk_user_employee_id UNIQUE (employee_id)
);

CREATE INDEX IF NOT EXISTS idx_user_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_user_department_id ON users(department_id);
CREATE INDEX IF NOT EXISTS idx_user_role_id ON users(role_id);

-- Attendances table
CREATE TABLE IF NOT EXISTS attendances (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    date DATE NOT NULL,
    check_in_time TIMESTAMP,
    check_out_time TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    total_hours DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    late_approved BOOLEAN NOT NULL DEFAULT false,
    approved_by UUID,
    approved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_attendance_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_attendance_approved_by FOREIGN KEY (approved_by) REFERENCES users(id),
    CONSTRAINT uk_user_date UNIQUE (user_id, date)
);

CREATE INDEX IF NOT EXISTS idx_attendance_user_date ON attendances(user_id, date);
CREATE INDEX IF NOT EXISTS idx_attendance_date ON attendances(date);
CREATE INDEX IF NOT EXISTS idx_attendance_status ON attendances(status);
CREATE INDEX IF NOT EXISTS idx_attendance_user_id ON attendances(user_id);

