import type { User } from "@/shared/api";
import { api } from "../base";

const baseURL = "/api/v1/users";

export const userService = {
  getCurrentUser: async (): Promise<User> => {
    const { data } = await api.get(`${baseURL}/me`);
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
