CREATE SCHEMA IF NOT EXISTS de_sprint;

CREATE TYPE de_sprint.action_type AS ENUM ('visit', 'click', 'scroll', 'move');

CREATE TABLE IF NOT EXISTS de_sprint.actions(
    id INT NOT NULL,
    timestamp BIGINT NOT NULL,
    type de_sprint.action_type NOT NULL,
    page_id INT NOT NULL,
    tag TEXT NOT NULL,
    sign BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS de_sprint.profiles(
    id INT GENERATED ALWAYS AS IDENTITY primary key,
    user_id int NOT NULL UNIQUE,
    full_name TEXT NOT NULL,
    birthdate DATE,
    created_at date DEFAULT NOW()
);


INSERT INTO de_sprint.actions
	(id, "timestamp", "type", page_id, tag, sign)
VALUES
	(1, 946683757, 'visit', 1, 'спорт', true),
	(1, 946687357, 'click', 2, 'спорт', true),
	(3, 946784557, 'scroll', 3, 'медицина', true),
	(4, 946975357, 'move', 3, 'медицина', false);
	
INSERT INTO de_sprint.profiles
	(user_id, full_name, birthdate, created_at)
VALUES
	(1, 'Логинов Алексей Алексеевич', to_date('1980-01-01', 'YYYY-MM-DD'), to_date('1999-04-01', 'YYYY-MM-DD')),
	(2, 'Калугин Максим Львович', to_date('1980-01-01', 'YYYY-MM-DD'), to_date('1999-05-19', 'YYYY-MM-DD')),
	(3, 'Кудрявцева Варвара Денисовна', to_date('1980-01-01', 'YYYY-MM-DD'), to_date('1999-09-05', 'YYYY-MM-DD')),
	(4, 'Титова Василиса Сергеевна', to_date('1980-01-01', 'YYYY-MM-DD'), to_date('1999-12-01', 'YYYY-MM-DD')),
	(5, 'Смирнова Екатерина Львовна', to_date('1980-01-01', 'YYYY-MM-DD'), to_date('1999-01-01', 'YYYY-MM-DD'));

