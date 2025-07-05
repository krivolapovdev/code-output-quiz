import type { User } from "@/shared/api/user";
import { getJwtPayloadFromToken } from "../jwt";
import { useUserStore } from "../store";

export const initAuth = () => {
  const token = localStorage.getItem("accessToken");

  if (!token) {
    return;
  }

  const payload = getJwtPayloadFromToken(token);

  if (payload) {
    const user: User = {
      id: payload.id,
      email: payload.email
    };
    useUserStore.getState().setUser(user);
  }
};
