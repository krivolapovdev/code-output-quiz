DELETE
FROM quizzes AS q
WHERE q.id IN (SELECT quiz_id
               FROM quiz_answer_choices
               GROUP BY quiz_id, LOWER(BTRIM(text))
               HAVING COUNT(*) > 1);

ALTER TABLE quiz_answer_choices
    ADD CONSTRAINT quiz_answer_choices_text_unique
        UNIQUE (quiz_id, text);
