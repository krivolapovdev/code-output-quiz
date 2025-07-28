import { userService } from "@/shared/api";
import { useUserStore } from "../store";

export const handleAuthSuccess = async () => {
  try {
    const user = await userService.getCurrentUser();
    useUserStore.getState().setUser(user);
  } catch (e) {
    console.error("Failed to fetch current user:", e);
    useUserStore.getState().setUser(null);
  }
};

export const handleAuthError = (error: Error) => {
  console.error("Authentication failed:", error);
  useUserStore.getState().setUser(null);
};
