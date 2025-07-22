import { api } from "../base";
import type { User } from "./types";

const baseURL = "/api/v1/users";

export const userService = {
  getUser: async (): Promise<User> => {
    const { data } = await api.get<User>(`${baseURL}/me`);
    return data;
  },

  addUserSolvedQuiz: async (
    quizId: string,
    selectedAnswer: string
  ): Promise<void> => {
    await api.post<void>(`${baseURL}/me/solved-quizzes`, {
      quizId,
      selectedAnswer
    });
  }
};
