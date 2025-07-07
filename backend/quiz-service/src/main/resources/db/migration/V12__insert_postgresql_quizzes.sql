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
        'Alice',
        'Bob',
        'Charlie',
        'Alice, Charlie'
     );

SELECT insert_quiz_with_choices(
        $code$SELECT NOW();$code$,
        'POSTGRESQL',
        'BEGINNER',
        'C',
        'The NOW() function returns the current timestamp, which includes both the date and time.',
        'The current time in UTC only',
        'The current date only',
        'The current date and time',
        'The time 24 hours from now'
     );

SELECT insert_quiz_with_choices(
        $code$SELECT COALESCE(NULL, 'default', 'backup');$code$,
        'POSTGRESQL',
        'BEGINNER',
        'B',
        'COALESCE returns the first non-null value. NULL is skipped, and "default" is the first valid result.',
        'NULL',
        'default',
        'backup',
        'Error'
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
        'Alice only',
        'Bob only',
        'Alice and Bob',
        'No rows returned'
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
        'Counts all sales in the sales table',
        'Counts only sales made today',
        'Counts sales made in the last 30 days',
        'Returns an error because of incorrect syntax'
     );

SELECT insert_quiz_with_choices(
        $code$SELECT DISTINCT ON (department) department, name, salary
FROM employees
ORDER BY department, salary DESC;$code$,
        'POSTGRESQL',
        'INTERMEDIATE',
        'B',
        'DISTINCT ON selects the first row for each department based on the ORDER BY clause. Since salary is ordered DESC, the highest-paid employee is selected per department.',
        'Returns all employees sorted by department and salary',
        'Returns the highest-paid employee in each department',
        'Returns one random employee per department',
        'Returns all employees with unique salaries'
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
        'Logs inserts into the users table',
        'Logs updates to any table',
        'Logs updates to rows in the users table into audit_log',
        'Prevents updates to the users table'
     );
