import { useEffect, useState } from "react";
import { quizService } from "@/shared/api";
import { useQuizStore } from "@/shared/lib/store";

export const useRandomQuiz = () => {
  const { difficultyLevel, programmingLanguage, quiz, setQuiz } =
    useQuizStore();
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
  }, [difficultyLevel, programmingLanguage, setQuiz]);

  return { quiz, difficultyLevel, programmingLanguage, error, loading };
};
