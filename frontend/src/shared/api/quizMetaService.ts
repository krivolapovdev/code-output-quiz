import { api } from "./base";

const baseURL = "/api/v1/quizzes/meta";

export const quizMetaService = {
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
