------------------------------- 2.3 Версия Lite -------------------------------

-- Уникальный номер сотрудника, его ФИО и стаж работы – для всех сотрудников компании
SELECT 
	id, 
	full_name, 
	CURRENT_DATE-start_date as days_experience,
	DATE_PART('year', now()) - DATE_PART('year', start_date) as year_experience
FROM de_sprint.employees;

-- Уникальный номер сотрудника, его ФИО и стаж работы – только первых 3-х сотрудников
SELECT 
	id, 
	full_name, 
	CURRENT_DATE-start_date as days_experience,
	DATE_PART('year', now()) - DATE_PART('year', start_date) as year_experience
FROM de_sprint.employees
LIMIT 3;

-- Уникальный номер сотрудников - водителей
SELECT
	id
FROM de_sprint.employees
WHERE driver_license;

-- Выведите номера сотрудников, которые хотя бы за 1 квартал получили оценку D или E
SELECT DISTINCT
	employee_id
FROM de_sprint.grades
WHERE grade in ('D', 'E');

-- Выведите самую высокую зарплату в компании.
SELECT
	MAX(salary)
FROM de_sprint.employees;

------------------------------- 2.4 Версия Lite -------------------------------

-- a. Попробуйте вывести не просто самую высокую зарплату во всей команде, а вывести именно фамилию сотрудника с самой высокой зарплатой.
SELECT
	salary,
	full_name
FROM de_sprint.employees
ORDER BY salary desc
LIMIT 1;

-- b. Попробуйте вывести фамилии сотрудников в алфавитном порядке
SELECT
	full_name
FROM de_sprint.employees
ORDER BY full_name asc;

-- c. Рассчитайте средний стаж для каждого уровня сотрудников
SELECT 
	position, 
	avg(DATE_PART('year', now()) - DATE_PART('year', start_date)) as avg_experience
FROM de_sprint.employees
GROUP BY position;

-- d. Выведите фамилию сотрудника и название отдела, в котором он работает
SELECT
	e.full_name,
	d.title
FROM de_sprint.employees e JOIN de_sprint.departments d ON e.department_id = d.id;

-- e. Выведите название отдела и фамилию сотрудника с самой высокой зарплатой в данном отделе и саму зарплату также.
SELECT
	dep.title,
	e.full_name,
	d.salary
FROM de_sprint.employees e 
JOIN (
		SELECT
			max(e.salary) salary,
			e.department_id	
		FROM de_sprint.employees e
		GROUP BY e.department_id
	) d ON e.department_id = d.department_id
JOIN de_sprint.departments dep on dep.id = d.department_id
WHERE d.salary = e.salary
GROUP BY dep.title, e.full_name, d.salary;

