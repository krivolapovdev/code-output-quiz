import { useMutation } from "@tanstack/react-query";
import { authService } from "@/shared/api";
import type { RegisterFormValues } from "./schema";

export const useRegisterMutation = () => {
  return useMutation({
    mutationFn: (data: RegisterFormValues) => authService.register(data),
    onSuccess: data => {
      localStorage.setItem("accessToken", data.accessToken);
    },
    onError: error => {
      console.error("Registration failed:", error);
    }
  });
};
