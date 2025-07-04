import { create } from "zustand";
import type { QuizResponse } from "@/shared/api/quiz/types";

type QuizStore = {
  quiz: QuizResponse | null;
  programmingLanguage: string;
  difficultyLevel: string;

  setQuiz: (quiz: QuizResponse) => void;
  setDifficulty: (level: string) => void;
  setLanguage: (lang: string) => void;
};

export const useQuizStore = create<QuizStore>(set => ({
  quiz: null,
  difficultyLevel: "BEGINNER",
  programmingLanguage: "JAVA",

  setQuiz: quiz => set({ quiz }),
  setDifficulty: level => set({ difficultyLevel: level }),
  setLanguage: lang => set({ programmingLanguage: lang })
}));
