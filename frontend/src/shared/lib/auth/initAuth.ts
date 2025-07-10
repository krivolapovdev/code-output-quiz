import type { User } from "@/shared/api/user";
import { getJwtPayloadFromToken } from "../jwt";
import { useUserStore } from "../store";

export const initAuth = () => {
  const token = localStorage.getItem("accessToken");

  if (!token) {
    return;
  }

  const payload = getJwtPayloadFromToken(token);
  console.log("Payload from token:", payload);

  if (payload) {
    const user: User = {
      id: payload.userId,
      email: payload.sub
    };
    console.log("User initialized from token:", user);

    useUserStore.getState().setUser(user);
  }
};
