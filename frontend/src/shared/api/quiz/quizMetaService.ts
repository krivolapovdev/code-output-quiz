import { api } from "../base";

const baseURL = "/api/v1/quizzes/meta";

export const quizMetaService = {
  fetchSupportedProgrammingLanguages: async (): Promise<string[]> => {
    const { data } = await api.get<string[]>(
      `${baseURL}/supported-programming-languages`
    );
    return data;
  },

  fetchSupportedDifficultyLevels: async (): Promise<string[]> => {
    const { data } = await api.get<string[]>(
      `${baseURL}/supported-difficulty-levels`
    );
    return data;
  }
};
