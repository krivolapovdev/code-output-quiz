import { useNextQuiz } from "@/features/fetch-next-quiz";
import { authService } from "@/shared/api";
import { useUserStore } from "@/shared/lib/store";

export const useLogout = () => {
  const { refetch } = useNextQuiz();

  return async () => {
    await authService.logout();
    useUserStore.getState().setUser(null);
    await refetch();
  };
};
