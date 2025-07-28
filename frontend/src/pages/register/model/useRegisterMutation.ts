import { useMutation } from "@tanstack/react-query";
import { authService } from "@/shared/api";
import { handleAuthError, handleAuthSuccess } from "@/shared/lib/auth/auth";
import type { RegisterFormValues } from "./schema";

export const useRegisterMutation = () => {
  return useMutation({
    mutationFn: (data: RegisterFormValues) => authService.register(data),
    onSuccess: _ => handleAuthSuccess(),
    onError: error => handleAuthError(error)
  });
};
