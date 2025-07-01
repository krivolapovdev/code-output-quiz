CREATE TABLE solved_quizzes
(
    user_id   UUID NOT NULL,
    quiz_id   UUID NOT NULL,
    solved_at TIMESTAMPTZ DEFAULT now(),
    PRIMARY KEY (user_id, quiz_id)
);
