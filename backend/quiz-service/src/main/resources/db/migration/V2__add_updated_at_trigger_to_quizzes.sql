CREATE OR REPLACE FUNCTION touch_updated_at_column()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$;

CREATE TRIGGER quizzes_touch_updated_at_trigger
    BEFORE UPDATE
    ON quizzes
    FOR EACH ROW
EXECUTE FUNCTION touch_updated_at_column();
