CREATE TABLE solved_quizzes
(
    user_id UUID NOT NULL,
    quiz_id UUID NOT NULL,
    CONSTRAINT solved_quizzes_pk PRIMARY KEY (user_id, quiz_id)
);
