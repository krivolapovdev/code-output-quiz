CREATE TYPE answer_choice AS ENUM ('A', 'B', 'C', 'D');

CREATE TABLE user_solved_quizzes
(
    user_id         UUID          NOT NULL,
    quiz_id         UUID          NOT NULL,
    selected_answer answer_choice NOT NULL,
    solved_at       TIMESTAMPTZ DEFAULT now(),
    CONSTRAINT user_solved_quizzes_pk PRIMARY KEY (user_id, quiz_id)
);

CREATE TABLE user_quiz_reactions
(
    user_id    UUID    NOT NULL,
    quiz_id    UUID    NOT NULL,
    liked      BOOLEAN NOT NULL,
    reacted_at TIMESTAMPTZ DEFAULT now(),
    CONSTRAINT user_quiz_reactions_pk PRIMARY KEY (user_id, quiz_id)
);
