------------------------------- 2.3 -------------------------------

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

--* Выведите название самого крупного отдела
SELECT
	title
FROM
	de_sprint.departments
WHERE
	amount = (
	SELECT
		max(amount)
	FROM
		de_sprint.departments);

--* Выведите номера сотрудников от самых опытных до вновь прибывших
SELECT
	id
FROM
	de_sprint.employees
ORDER BY
	CURRENT_DATE-start_date DESC;
		
--* Рассчитайте среднюю зарплату для каждого уровня сотрудников
SELECT
	"level",
	avg(salary)
FROM
	de_sprint.employees
GROUP BY
	"level";
	
/*
* Добавьте столбец с информацией о коэффициенте годовой премии к основной таблице. Коэффициент рассчитывается по такой схеме: базовое значение коэффициента – 1, каждая оценка действует на коэффициент так:
	· Е – минус 20%
	· D – минус 10%
	· С – без изменений
	· B – плюс 10%
	· A – плюс 20%
*/
WITH aux AS 
(
SELECT
	employee_id,
	1 + sum(
	CASE
		WHEN grade = 'A' THEN 0.2
		WHEN grade = 'B' THEN 0.1
		WHEN grade = 'D' THEN -0.1
		WHEN grade = 'E' THEN -0.2
		ELSE 0.0
	END) AS bonus_score
FROM
	de_sprint.grades
GROUP BY
	employee_id)
SELECT
	e.*,
	a.bonus_score
FROM
	de_sprint.employees e
JOIN aux a ON
	e.id = a.employee_id;

	
------------------------------- 2.4 -------------------------------

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

-- f. *Выведите название отдела, сотрудники которого получат наибольшую премию по итогам года. Как рассчитать премию можно узнать в последнем задании предыдущей домашней работы
WITH grade_aux AS 
(
SELECT
	employee_id,
	1 + sum(
	CASE
		WHEN grade = 'A' THEN 0.2
		WHEN grade = 'B' THEN 0.1
		WHEN grade = 'D' THEN -0.1
		WHEN grade = 'E' THEN -0.2
		ELSE 0.0
	END) AS bonus_score
FROM
	de_sprint.grades
GROUP BY
	employee_id),
employee_aux AS 
(
SELECT
	employee_id
FROM
	grade_aux
WHERE
	bonus_score = (
	SELECT
		max(bonus_score)
	FROM
		grade_aux) 
)
SELECT
	d.title
FROM
	de_sprint.departments d
WHERE
	d.id =
(
	SELECT
		e.department_id
	FROM
		de_sprint.employees e
	WHERE
		e.id = (
		SELECT
			employee_id
		FROM
			employee_aux)

)

-- g. *Проиндексируйте зарплаты сотрудников с учетом коэффициента премии. Для сотрудников с коэффициентом премии больше 1.2 – размер индексации составит 20%, для сотрудников с коэффициентом премии от 1 до 1.2 размер индексации составит 10%. Для всех остальных сотрудников индексация не предусмотрена.
WITH grade_aux AS 
(
SELECT
	employee_id,
	1 + sum(
	CASE
		WHEN grade = 'A' THEN 0.2
		WHEN grade = 'B' THEN 0.1
		WHEN grade = 'D' THEN -0.1
		WHEN grade = 'E' THEN -0.2
		ELSE 0.0
	END) AS bonus_score
FROM
	de_sprint.grades
GROUP BY
	employee_id),
employees_aux AS (
SELECT
	id,
	full_name,
	birthdate,
	start_date,
	"position",
	"level",
	department_id,
	driver_license,
	salary,
	g.bonus_score,
	CASE 
		WHEN bonus_score > 1.2 THEN 1.2 * salary
		WHEN bonus_score < 1.2
		AND bonus_score > 1 THEN 1.1 * salary
		ELSE salary
	END salary_indexed
FROM
	de_sprint.employees e
JOIN grade_aux g ON
	e.id = g.employee_id)
	SELECT
	-- i.     Название отдела
	title,
	-- ii.     Фамилию руководителя
	director,
	-- iii.     Количество сотрудников
	amount,
	-- iv.     Средний стаж
	(
	SELECT
		avg(DATE_PART('year', now()) - DATE_PART('year', start_date)) AS avg_experience
	FROM
		de_sprint.employees e
	WHERE
		e.department_id = d.id
	GROUP BY
		e.department_id) avg_experience,
	-- v.     Средний уровень зарплаты
	(
	SELECT
		avg(salary) AS avg_salary
	FROM
		de_sprint.employees e
	WHERE
		e.department_id = d.id
	GROUP BY
		e.department_id) avg_salary,
	-- vi.     Количество сотрудников уровня junior
	(
	SELECT
		count(1)
	FROM
		de_sprint.employees e
	WHERE
		e.department_id = d.id
		AND "level" = 'junior'
	GROUP BY
		e.department_id) cnt_junior,
	-- vii.     Количество сотрудников уровня middle
	(
	SELECT
		count(1)
	FROM
		de_sprint.employees e
	WHERE
		e.department_id = d.id
		AND "level" = 'middle'
	GROUP BY
		e.department_id) cnt_middle,
	-- viii.     Количество сотрудников уровня senior
	(
	SELECT
		count(1)
	FROM
		de_sprint.employees e
	WHERE
		e.department_id = d.id
		AND "level" = 'senior'
	GROUP BY
		e.department_id) cnt_senior,
	-- ix.     Количество сотрудников уровня lead
	(
	SELECT
		count(1)
	FROM
		de_sprint.employees e
	WHERE
		e.department_id = d.id
		AND "level" = 'lead'
	GROUP BY
		e.department_id) cnt_lead,
	-- x.     Общий размер оплаты труда всех сотрудников до индексации
	(
	SELECT
		sum(salary)
	FROM
		employees_aux e
	WHERE
		e.department_id = d.id
	GROUP BY
		e.department_id) salary_total,
	--xi.     Общий размер оплаты труда всех сотрудников после индексации
	(
	SELECT
		sum(salary_indexed)
	FROM
		employees_aux e
	WHERE
		e.department_id = d.id
	GROUP BY
		e.department_id) salary_indexed_total,
	-- xii.     Общее количество оценок А
	(
	SELECT
		count(1)
	FROM
		de_sprint.grades g
	JOIN de_sprint.employees e ON
		e.id = g.employee_id
	WHERE
		e.department_id = d.id
		AND grade = 'A'
	) a_count,
	-- xiii.     Общее количество оценок B
	(
	SELECT
		count(1)
	FROM
		de_sprint.grades g
	JOIN de_sprint.employees e ON
		e.id = g.employee_id
	WHERE
		e.department_id = d.id
		AND grade = 'B'
	) b_count,
	-- xiv.     Общее количество оценок C
	(
	SELECT
		count(1)
	FROM
		de_sprint.grades g
	JOIN de_sprint.employees e ON
		e.id = g.employee_id
	WHERE
		e.department_id = d.id
		AND grade = 'C'
	) c_count,
	-- xv.     Общее количество оценок D
	(
	SELECT
		count(1)
	FROM
		de_sprint.grades g
	JOIN de_sprint.employees e ON
		e.id = g.employee_id
	WHERE
		e.department_id = d.id
		AND grade = 'D'
	) d_count,
	-- xvi.     Общее количество оценок Е
	(
	SELECT
		count(1)
	FROM
		de_sprint.grades g
	JOIN de_sprint.employees e ON
		e.id = g.employee_id
	WHERE
		e.department_id = d.id
		AND grade = 'E'
	) e_count,
	-- xvii.     Средний показатель коэффициента премии
	(
	SELECT
		avg(g.bonus_score)
	FROM
		grade_aux g
	JOIN employees_aux e ON
		e.id = g.employee_id
	WHERE
		e.department_id = d.id
	) avg_bonus_score,
	-- xviii.     Общий размер премии.
	(
	SELECT
		sum(salary * g.bonus_score)
	FROM
		grade_aux g
	JOIN employees_aux e ON
		e.id = g.employee_id
	WHERE
		e.department_id = d.id
	) bonus_total,
	-- xix.     Общую сумму зарплат(+ премии) до индексации
	(
	SELECT
		sum(salary) + sum(salary * bonus_score)
	FROM
		employees_aux e
	WHERE
		e.department_id = d.id
	GROUP BY
		e.department_id) salary_plus_bonus_total,
	-- xx.     Общую сумму зарплат(+ премии) после индексации(премии не индексируются)
	(
	SELECT
		sum(salary_indexed) + sum(salary * bonus_score)
	FROM
		employees_aux e
	WHERE
		e.department_id = d.id
	GROUP BY
		e.department_id) salary_indexed_plus_bonus_total,
	-- xxi.     Разницу в % между предыдущими двумя суммами(первая/вторая)
	(
	SELECT
		(sum(salary) + sum(salary * bonus_score)) / (sum(salary_indexed) + sum(salary * bonus_score))
	FROM
		employees_aux e
	WHERE
		e.department_id = d.id
	GROUP BY
		e.department_id) percent_difference
FROM
	de_sprint.departments d;







