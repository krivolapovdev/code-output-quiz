import { userService } from "@/shared/api";
import { useUserStore } from "@/shared/lib/store";

export const useReportAnswer = () => {
  const { user } = useUserStore();

  return async (quizId: string, selectedAnswer: string) => {
    if (!user) {
      console.log("User is not authenticated; answer will not be reported.");
      return;
    }

    try {
      await userService.addUserSolvedQuiz(quizId, selectedAnswer);
      console.log("Answer reported successfully");
    } catch (error) {
      console.error("Failed to report answer", error);
    }
  };
};
