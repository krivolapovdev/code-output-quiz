CREATE TYPE answer_choice AS ENUM ('A', 'B', 'C', 'D');

CREATE TABLE user_solved_quizzes
(
    user_id         UUID          NOT NULL,
    quiz_id         UUID          NOT NULL,
    selected_answer answer_choice NOT NULL,
    solved_at       TIMESTAMPTZ DEFAULT now(),
    CONSTRAINT user_solved_quizzes_pk PRIMARY KEY (user_id, quiz_id)
);
