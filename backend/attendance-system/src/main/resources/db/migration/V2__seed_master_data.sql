-- Roles
INSERT INTO roles (id, name)
VALUES (gen_random_uuid(), 'EMPLOYEE')
ON CONFLICT (name) DO NOTHING;

INSERT INTO roles (id, name)
VALUES (gen_random_uuid(), 'MANAGER')
ON CONFLICT (name) DO NOTHING;

-- Department
INSERT INTO departments (id, name)
VALUES (gen_random_uuid(), 'IT')
ON CONFLICT (name) DO NOTHING;
