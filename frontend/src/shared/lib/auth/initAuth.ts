import { userService } from "@/shared/api";
import { useUserStore } from "@/shared/lib/store";

export const initAuth = async () => {
  const { setUser } = useUserStore.getState();

  try {
    const user = await userService.getCurrentUser();
    setUser(user);
  } catch (error) {
    console.error("initAuth failed:", error);
    setUser(null);
  }
};
