import { useMutation } from "@tanstack/react-query";
import { authService } from "@/shared/api";
import { handleAuthError, handleAuthSuccess } from "@/shared/lib/auth/auth";
import type { LoginFormValues } from "./schema";

export const useLoginMutation = () => {
  return useMutation({
    mutationFn: (payload: LoginFormValues) => authService.login(payload),
    onSuccess: data => handleAuthSuccess(data.accessToken),
    onError: error => handleAuthError(error)
  });
};
