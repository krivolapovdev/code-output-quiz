import { useMutation } from "@tanstack/react-query";
import { authService } from "@/shared/api/authService";
import type { LoginFormValues } from "./schema";

export const useLoginMutation = () => {
  return useMutation({
    mutationFn: (payload: LoginFormValues) => authService.login(payload),
    onSuccess: data => {
      localStorage.setItem("accessToken", data.accessToken);
    },
    onError: error => {
      console.error("Registration failed:", error);
    }
  });
};
