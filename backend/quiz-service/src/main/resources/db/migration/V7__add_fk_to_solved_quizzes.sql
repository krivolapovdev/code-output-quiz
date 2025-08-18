ALTER TABLE solved_quizzes
    ADD CONSTRAINT solved_quizzes_quiz_id_fk
        FOREIGN KEY (quiz_id)
            REFERENCES quizzes (id)
            ON DELETE CASCADE;
