CREATE OR REPLACE FUNCTION insert_quiz_with_choices(
    quiz_code TEXT,
    lang_name TEXT,
    level_name TEXT,
    correct_answer answer_choice,
    explanation TEXT,
    answer_choices_json JSONB
) RETURNS VOID AS
$$
DECLARE
    quiz_id     UUID;
    answer_item RECORD;
BEGIN
    WITH q AS (
        INSERT INTO quizzes (
                             code,
                             correct_answer,
                             explanation,
                             programming_language_id,
                             difficulty_level_id
            )
            VALUES (quiz_code,
                    correct_answer,
                    explanation,
                    (SELECT id FROM programming_languages WHERE name = lang_name),
                    (SELECT id FROM difficulty_levels WHERE level = level_name))
            RETURNING id)
    SELECT id
    INTO quiz_id
    FROM q;

    FOR answer_item IN
        SELECT *
        FROM jsonb_to_recordset(answer_choices_json)
                 AS answer(choice answer_choice, text TEXT)
        LOOP
            INSERT INTO quiz_answer_choices (quiz_id, choice, text)
            VALUES (quiz_id,
                    answer_item.choice,
                    answer_item.text);
        END LOOP;
END;
$$ LANGUAGE plpgsql;
