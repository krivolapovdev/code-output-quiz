import { create } from "zustand";
import type { QuizResponse } from "@/shared/api/quiz/types";

type QuizStore = {
  quiz: QuizResponse | null;
  programmingLanguage: string;
  difficultyLevel: string;

  setQuiz: (quiz: QuizResponse) => void;
  setDifficultyLevel: (level: string) => void;
  setProgrammingLanguage: (lang: string) => void;
};

export const useQuizStore = create<QuizStore>(set => ({
  quiz: null,
  difficultyLevel: "BEGINNER",
  programmingLanguage: "JAVA",

  setQuiz: quiz => set({ quiz }),
  setDifficultyLevel: difficultyLevel => set({ difficultyLevel }),
  setProgrammingLanguage: programmingLanguage => set({ programmingLanguage })
}));
