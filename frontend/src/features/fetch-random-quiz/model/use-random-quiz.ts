import { useEffect, useState } from "react";
import type { QuizResponse } from "@/shared/api/quiz";
import { quizService } from "@/shared/api/quiz";
import { useQuizStore } from "@/shared/lib/store";

export const useRandomQuiz = () => {
  const { difficultyLevel, programmingLanguage } = useQuizStore();
  const [quiz, setQuiz] = useState<QuizResponse | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadQuiz = async () => {
      setLoading(true);
      setError(null);
      try {
        const result = await quizService.getRandomQuiz(
          programmingLanguage,
          difficultyLevel
        );
        setQuiz(result);
      } catch (e: unknown) {
        if (e instanceof Error) {
          setError(e.message);
        } else {
          setError("Unknown error");
        }
      } finally {
        setLoading(false);
      }
    };

    loadQuiz();
  }, [difficultyLevel, programmingLanguage]);

  return { quiz, error, loading };
};
