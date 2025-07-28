import { useNavigate } from "react-router-dom";
import { authService } from "@/shared/api";
import { useUserStore } from "@/shared/lib/store";

export const useLogout = () => {
  const navigate = useNavigate();
  const { setUser } = useUserStore();

  return async () => {
    try {
      await authService.logout();
    } catch (error) {
      console.error("Logout request failed", error);
    }

    setUser(null);
    navigate("/");
  };
};
