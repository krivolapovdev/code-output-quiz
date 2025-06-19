import { api } from "@/shared/api";

const baseURL = "/v1/quizzes";

export const quizService = {
  fetchSupportedProgrammingLanguages: async (): Promise<string[]> => {
    const res = await api.get<string[]>(
      `${baseURL}/supported-programming-languages`
    );
    return res.data;
  },

  fetchSupportedDifficultyLevels: async (): Promise<string[]> => {
    const res = await api.get<string[]>(
      `${baseURL}/supported-difficulty-levels`
    );
    return res.data;
  }
};
