CREATE OR REPLACE FUNCTION uuidv7(P_TIMESTAMP TIMESTAMP WITH TIME ZONE DEFAULT CLOCK_TIMESTAMP())
    RETURNS UUID
    LANGUAGE PLPGSQL
AS
$$
DECLARE
    v_time       DOUBLE PRECISION;
    v_unix_t     BIGINT;
    v_rand_a     BIGINT;
    v_rand_b     BIGINT;
    v_unix_t_hex VARCHAR;
    v_rand_a_hex VARCHAR;
    v_rand_b_hex VARCHAR;
    c_milli      DOUBLE PRECISION := 1_000;
    c_micro      DOUBLE PRECISION := 1_000_000;
    c_scale      DOUBLE PRECISION := 4.096; -- 4.0 * (1024 / 1000)

    c_version    BIGINT           := X'0000000000007000'::BIGINT; -- RFC 9562 version: 0111
    c_variant    BIGINT           := X'8000000000000000'::BIGINT; -- RFC 9562 variant: 10xx
BEGIN
    v_time := EXTRACT(EPOCH FROM P_TIMESTAMP);

    v_unix_t := TRUNC(v_time * c_milli);
    v_rand_a := TRUNC((v_time * c_micro - v_unix_t * c_milli) * c_scale);
    v_rand_b := (TRUNC(RANDOM() * 2 ^ 30)::BIGINT << 32) | TRUNC(RANDOM() * 2 ^ 32)::BIGINT;

    v_unix_t_hex := LPAD(TO_HEX(v_unix_t), 12, '0');
    v_rand_a_hex := LPAD(TO_HEX((v_rand_a | c_version)::BIGINT), 4, '0');
    v_rand_b_hex := LPAD(TO_HEX((v_rand_b | c_variant)::BIGINT), 16, '0');

    RETURN (v_unix_t_hex || v_rand_a_hex || v_rand_b_hex)::UUID;
END;
$$;

CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$;

CREATE TABLE IF NOT EXISTS programming_languages
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS difficulty_levels
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    level TEXT UNIQUE NOT NULL
);

CREATE TYPE answer_choice AS ENUM ('A', 'B', 'C', 'D');

CREATE TABLE IF NOT EXISTS quizzes
(
    id                      UUID        DEFAULT uuidv7() PRIMARY KEY,
    code                    TEXT          NOT NULL UNIQUE,
    correct_answer          answer_choice NOT NULL,
    explanation             TEXT          NOT NULL,
    programming_language_id BIGINT        NOT NULL REFERENCES programming_languages (id),
    difficulty_level_id     BIGINT        NOT NULL REFERENCES difficulty_levels (id),
    created_at              TIMESTAMPTZ DEFAULT now(),
    updated_at              TIMESTAMPTZ DEFAULT now()
);

CREATE TRIGGER update_quizzes_updated_at
    BEFORE UPDATE
    ON quizzes
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE OR REPLACE VIEW quizzes_view AS
SELECT q.id,
       q.code,
       q.correct_answer,
       q.explanation,
       dl.level AS difficulty_level,
       pl.name  AS programming_language,
       q.created_at,
       q.updated_at
FROM quizzes AS q
         JOIN difficulty_levels AS dl ON q.difficulty_level_id = dl.id
         JOIN programming_languages AS pl ON q.programming_language_id = pl.id;

INSERT INTO programming_languages (name)
VALUES ('JAVA'),
       ('PYTHON'),
       ('C'),
       ('CPP'),
       ('GO'),
       ('CSHARP'),
       ('JAVASCRIPT'),
       ('RUST'),
       ('SWIFT'),
       ('PHP');

INSERT INTO difficulty_levels (level)
VALUES ('BEGINNER'),
       ('INTERMEDIATE'),
       ('ADVANCED');

INSERT INTO quizzes (code, correct_answer, explanation, programming_language_id,
                     difficulty_level_id)
VALUES ('quiz_java_main', 'A', 'sample', 1, 1),
       ('quiz_python_def', 'B', 'temp', 2, 2);
