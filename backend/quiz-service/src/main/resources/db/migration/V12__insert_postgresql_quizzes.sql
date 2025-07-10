SELECT insert_quiz_with_choices(
               $code$CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    age INTEGER
);

INSERT INTO users (name, age)
VALUES ('Alice', 30), ('Bob', 25), ('Charlie', 35);

SELECT name FROM users WHERE age > 30;$code$,
               'POSTGRESQL',
               'BEGINNER',
               'C',
               'The SELECT filters for users with age > 30. Only Charlie (age 35) matches, so only his name is returned.',
               array [
                   row ('A', 'Alice')::answer_choice_data,
                   row ('B', 'Bob')::answer_choice_data,
                   row ('C', 'Charlie')::answer_choice_data,
                   row ('D', 'Alice, Charlie')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$SELECT NOW();$code$,
               'POSTGRESQL',
               'BEGINNER',
               'C',
               'The NOW() function returns the current timestamp, which includes both the date and time.',
               array [
                   row ('A', 'The current time in UTC only')::answer_choice_data,
                   row ('B', 'The current date only')::answer_choice_data,
                   row ('C', 'The current date and time')::answer_choice_data,
                   row ('D', 'The time 24 hours from now')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$SELECT COALESCE(NULL, 'default', 'backup');$code$,
               'POSTGRESQL',
               'BEGINNER',
               'B',
               'COALESCE returns the first non-null value. NULL is skipped, and "default" is the first valid result.',
               array [
                   row ('A', 'NULL')::answer_choice_data,
                   row ('B', 'default')::answer_choice_data,
                   row ('C', 'backup')::answer_choice_data,
                   row ('D', 'Error')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    customer TEXT,
    amount NUMERIC
);

INSERT INTO orders (customer, amount)
VALUES
('Alice', 50),
('Bob', 100),
('Alice', 200);

SELECT customer, SUM(amount)
FROM orders
GROUP BY customer
HAVING SUM(amount) > 100;$code$,
               'POSTGRESQL',
               'INTERMEDIATE',
               'A',
               'The query groups orders by customer and filters with HAVING. Alice has a total of 250, Bob has 100. Only Alice meets the condition SUM(amount) > 100.',
               array [
                   row ('A', 'Alice only')::answer_choice_data,
                   row ('B', 'Bob only')::answer_choice_data,
                   row ('C', 'Alice and Bob')::answer_choice_data,
                   row ('D', 'No rows returned')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$WITH recent_sales AS (
    SELECT *
    FROM sales
    WHERE sale_date > CURRENT_DATE - INTERVAL '30 days'
)
SELECT COUNT(*)
FROM recent_sales;$code$,
               'POSTGRESQL',
               'INTERMEDIATE',
               'C',
               'The CTE filters rows where sale_date is within the last 30 days. The outer query counts them.',
               array [
                   row ('A', 'Counts all sales in the sales table')::answer_choice_data,
                   row ('B', 'Counts only sales made today')::answer_choice_data,
                   row ('C', 'Counts sales made in the last 30 days')::answer_choice_data,
                   row ('D', 'Returns an error because of incorrect syntax')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$SELECT DISTINCT ON (department) department, name, salary
FROM employees
ORDER BY department, salary DESC;$code$,
               'POSTGRESQL',
               'INTERMEDIATE',
               'B',
               'DISTINCT ON selects the first row for each department based on the ORDER BY clause. Since salary is ordered DESC, the highest-paid employee is selected per department.',
               array [
                   row ('A', 'Returns all employees sorted by department and salary')::answer_choice_data,
                   row ('B', 'Returns the highest-paid employee in each department')::answer_choice_data,
                   row ('C', 'Returns one random employee per department')::answer_choice_data,
                   row ('D', 'Returns all employees with unique salaries')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$CREATE OR REPLACE FUNCTION log_update()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO audit_log(table_name, changed_at)
    VALUES (TG_TABLE_NAME, now());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_users_update
AFTER UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION log_update();$code$,
               'POSTGRESQL',
               'ADVANCED',
               'C',
               'This trigger logs every row update in the "users" table to the "audit_log" table using a function. TG_TABLE_NAME gives the name of the modified table.',
               array [
                   row ('A', 'Logs inserts into the users table')::answer_choice_data,
                   row ('B', 'Logs updates to any table')::answer_choice_data,
                   row ('C', 'Logs updates to rows in the users table into audit_log')::answer_choice_data,
                   row ('D', 'Prevents updates to the users table')::answer_choice_data
                   ]
       );
