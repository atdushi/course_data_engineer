CREATE SCHEMA IF NOT EXISTS de_sprint;

CREATE TYPE de_sprint.level_type AS ENUM ('junior', 'middle', 'senior', 'lead');

CREATE TYPE de_sprint.grade_type AS ENUM ('A', 'B', 'C', 'D', 'E');

CREATE TABLE IF NOT EXISTS de_sprint.departments(
    id INT GENERATED ALWAYS AS IDENTITY primary key,
    title TEXT NOT NULL UNIQUE,
    director TEXT NOT NULL,
    amount smallint DEFAULT 0,
    created_at timestamptz DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS de_sprint.employees(
    id INT GENERATED ALWAYS AS IDENTITY primary key,
    full_name TEXT NOT NULL,
    birthdate DATE,
    start_date DATE NOT NULL,
    position TEXT NOT NULL,
    level de_sprint.level_type NOT NULL,
    salary FLOAT,
    department_id int,
    driver_license boolean DEFAULT FALSE,
    created_at timestamptz DEFAULT NOW(),
    updated_at timestamptz DEFAULT NOW(),
    CONSTRAINT department_fk
    	FOREIGN KEY (department_id)
    	REFERENCES de_sprint.departments(id)
    	ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS de_sprint.grades (
    employee_id int NOT NULL,
    quarter smallint,
    grade de_sprint.grade_type NOT NULL,
    created_at timestamptz DEFAULT NOW(),
    CONSTRAINT employee_fk
    	FOREIGN KEY (employee_id)
    	REFERENCES de_sprint.employees(id)
    	ON DELETE CASCADE
);


INSERT INTO 
	de_sprint.departments(title, director, amount) 
VALUES 
	('Бухгалтерия', 'Сафонова Алина Григорьевна', 1),
	('IT', 'Крючков Фёдор Артёмович', 4);


INSERT INTO 
	de_sprint.employees(full_name, birthdate, start_date, "position", level, salary, department_id, driver_license)
VALUES 
	('Сафонова Алина Григорьевна', '1990-01-08', '2008-01-08', 'Бухгалтер', 'lead', 50000, 1, true),
	('Антонов Евгений Артёмович', '1991-02-09', '2009-05-18', 'Программист', 'junior', 80000, 2, false),
	('Седов Антон Борисович', '1992-05-11', '2015-01-08', 'Программист', 'middle', 100000, 2, true),
	('Титова Ульяна Тимофеевна', '1993-01-08', '2020-01-22', 'Программист', 'senior', 110000, 2, false),
	('Крючков Фёдор Артёмович', '1994-10-08', '2000-05-26', 'Программист', 'lead', 130000, 2, true);
			

INSERT INTO 
	de_sprint.grades(employee_id, quarter, grade)
VALUES 
	(1, 1, 'A'),
	(1, 2, 'B'),
	(2, 1, 'E'),
	(2, 2, 'C'),
	(3, 1, 'B'),
	(3, 2, 'D'),
	(4, 1, 'A'),
	(4, 2, 'C'),
	(5, 1, 'E'),
	(5, 2, 'D');
