import type { User } from "@/shared/api/user";
import { getJwtPayloadFromToken } from "../jwt";
import { useUserStore } from "../store";

export const handleAuthSuccess = (accessToken: string) => {
  const { setUser } = useUserStore.getState();

  localStorage.setItem("accessToken", accessToken);

  const payload = getJwtPayloadFromToken(accessToken);
  if (payload != null) {
    const user: User = {
      id: payload.userId,
      email: payload.sub
    };
    setUser(user);
  }
};

export const handleAuthError = (error: Error) => {
  console.error("Registration failed:", error);

  const { clearUser } = useUserStore.getState();

  localStorage.removeItem("accessToken");
  clearUser();
};
