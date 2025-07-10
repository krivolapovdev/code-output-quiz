import { useCallback, useEffect, useState } from "react";
import { quizService } from "@/shared/api";
import { useQuizStore } from "@/shared/lib/store";

export const useRandomQuiz = () => {
  const { difficultyLevel, programmingLanguage, quiz, setQuiz } =
    useQuizStore();

  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  const fetchQuiz = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await quizService.getRandomQuiz(
        programmingLanguage,
        difficultyLevel
      );
      setQuiz(result);
    } catch (e) {
      setError(e instanceof Error ? e.message : "Unknown error");
    } finally {
      setLoading(false);
    }
  }, [programmingLanguage, difficultyLevel, setQuiz]);

  useEffect(() => {
    void fetchQuiz();
  }, [fetchQuiz]);

  return {
    quiz,
    difficultyLevel,
    programmingLanguage,
    error,
    loading,
    refetch: fetchQuiz
  };
};
