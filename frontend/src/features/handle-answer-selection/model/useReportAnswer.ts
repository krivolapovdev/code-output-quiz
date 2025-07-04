import { userService } from "@/shared/api/user";

export const useReportAnswer = () => {
  return async (quizId: string, selectedAnswer: string) => {
    try {
      await userService.addUserSolvedQuiz(quizId, selectedAnswer);
      console.log("Answer reported successfully");
    } catch (error) {
      console.error("Failed to report answer", error);
    }
  };
};
