import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { QuizResponse } from "@/shared/api";

type QuizStore = {
  quiz: QuizResponse | null;
  programmingLanguage: string;
  difficultyLevel: string;

  setQuiz: (quiz: QuizResponse) => void;
  setDifficultyLevel: (level: string) => void;
  setProgrammingLanguage: (lang: string) => void;
};

export const useQuizStore = create<QuizStore>()(
  persist(
    set => ({
      quiz: null,
      difficultyLevel: "ADVANCED",
      programmingLanguage: "JAVA",

      setQuiz: quiz => set({ quiz }),
      setDifficultyLevel: difficultyLevel => set({ difficultyLevel }),
      setProgrammingLanguage: programmingLanguage =>
        set({ programmingLanguage })
    }),
    {
      name: "quiz-store",
      partialize: state => ({
        programmingLanguage: state.programmingLanguage,
        difficultyLevel: state.difficultyLevel
      })
    }
  )
);
