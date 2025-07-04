import { api } from "../base";
import type { ProgrammingLanguageResponse } from "./types";

const baseURL = "/api/v1/quizzes/meta";

export const quizMetaService = {
  fetchSupportedProgrammingLanguages: async (): Promise<
    ProgrammingLanguageResponse[]
  > => {
    const { data } = await api.get<ProgrammingLanguageResponse[]>(
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
