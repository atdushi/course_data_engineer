CREATE SCHEMA IF NOT EXISTS de_sprint;

CREATE TYPE de_sprint.level AS ENUM ('junior', 'middle', 'senior', 'lead');

CREATE TYPE de_sprint.grade AS ENUM ('A', 'B', 'C', 'D', 'E');

CREATE TABLE IF NOT EXISTS de_sprint.employees(
    id serial primary key,
    full_name TEXT NOT NULL,
    birthdate DATE,
    start_date DATE NOT NULL,
    position TEXT NOT NULL,
    level de_sprint.level NOT NULL,
    salary FLOAT,
    department_id int,
    driver_license boolean DEFAULT FALSE,
    created_at timestamptz DEFAULT NOW(),
    updated_at timestamptz DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS de_sprint.departments(
    id serial primary key,
    title TEXT NOT NULL UNIQUE,
    director TEXT NOT NULL,
    amount int DEFAULT 0,
    created_at timestamptz DEFAULT NOW()
);


CREATE TABLE IF NOT EXISTS de_sprint.grades (
    employee_id int NOT NULL,
    quarter int,
    grade de_sprint.grade NOT NULL,
    created_at timestamptz DEFAULT NOW()
);

INSERT INTO de_sprint.departments(id, title, director, amount) VALUES (1, 'Бухгалтерия', 'Сафонова Алина Григорьевна', 1);
INSERT INTO de_sprint.departments(id, title, director, amount) VALUES (2, 'IT', 'Крючков Фёдор Артёмович', 4);

INSERT INTO de_sprint.employees(
	id, full_name, birthdate, start_date, "position", level, salary, department_id, driver_license)
	VALUES (1, 'Сафонова Алина Григорьевна', '1990-01-08', '2008-01-08', 'Бухгалтер', 'lead', 
			50000, 1, true);
			
INSERT INTO de_sprint.employees(
	id, full_name, birthdate, start_date, "position", level, salary, department_id, driver_license)
	VALUES (2, 'Антонов Евгений Артёмович', '1991-02-09', '2009-05-18', 'Программист', 'junior', 
			80000, 2, false);

INSERT INTO de_sprint.employees(
	id, full_name, birthdate, start_date, "position", level, salary, department_id, driver_license)
	VALUES (3, 'Седов Антон Борисович', '1992-05-11', '2015-01-08', 'Программист', 'middle', 
			100000, 2, true);
			
INSERT INTO de_sprint.employees(
	id, full_name, birthdate, start_date, "position", level, salary, department_id, driver_license)
	VALUES (4, 'Титова Ульяна Тимофеевна', '1993-01-08', '2020-01-22', 'Программист', 'senior', 
			110000, 2, false);
			
INSERT INTO de_sprint.employees(
	id, full_name, birthdate, start_date, "position", level, salary, department_id, driver_license)
	VALUES (5, 'Крючков Фёдор Артёмович', '1994-10-08', '2000-05-26', 'Программист', 'lead', 
			130000, 2, true);
			
INSERT INTO de_sprint.grades(employee_id, quarter, grade) VALUES (1, 1, 'A');
INSERT INTO de_sprint.grades(employee_id, quarter, grade) VALUES (1, 2, 'B');
INSERT INTO de_sprint.grades(employee_id, quarter, grade) VALUES (2, 1, 'E');
INSERT INTO de_sprint.grades(employee_id, quarter, grade) VALUES (2, 2, 'C');
INSERT INTO de_sprint.grades(employee_id, quarter, grade) VALUES (3, 1, 'B');
INSERT INTO de_sprint.grades(employee_id, quarter, grade) VALUES (3, 2, 'D');
INSERT INTO de_sprint.grades(employee_id, quarter, grade) VALUES (4, 1, 'A');
INSERT INTO de_sprint.grades(employee_id, quarter, grade) VALUES (4, 2, 'C');
INSERT INTO de_sprint.grades(employee_id, quarter, grade) VALUES (5, 1, 'E');
INSERT INTO de_sprint.grades(employee_id, quarter, grade) VALUES (5, 2, 'D');
