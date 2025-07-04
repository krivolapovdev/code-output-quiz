import { api } from "../base";
import type { QuizResponse } from "./types";

const baseURL = "/api/v1/quizzes";

export const quizService = {
  getRandomQuiz: async (
    programmingLanguage: string,
    difficultyLevel: string
  ): Promise<QuizResponse> => {
    const { data } = await api.get<QuizResponse>(`${baseURL}`, {
      params: {
        programmingLanguage,
        difficultyLevel
      }
    });

    return data;
  }
};
