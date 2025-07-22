CREATE TABLE user_quiz_reactions
(
    user_id    UUID    NOT NULL,
    quiz_id    UUID    NOT NULL,
    liked      BOOLEAN NOT NULL,
    reacted_at TIMESTAMPTZ DEFAULT now(),
    CONSTRAINT user_quiz_reactions_pk PRIMARY KEY (user_id, quiz_id)
);

CREATE OR REPLACE FUNCTION touch_reacted_at_column()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    NEW.reacted_at = now();
    RETURN NEW;
END;
$$;

CREATE TRIGGER user_quiz_reactions_touch_reacted_at_trigger
    BEFORE UPDATE
    ON user_quiz_reactions
    FOR EACH ROW
EXECUTE FUNCTION touch_reacted_at_column();
