import { useNavigate } from "react-router-dom";
import { useUserStore } from "@/shared/lib/store";

export const useLogout = () => {
  const navigate = useNavigate();
  const { clearUser } = useUserStore();

  return () => {
    localStorage.removeItem("accessToken");
    clearUser();
    navigate("/");
  };
};
