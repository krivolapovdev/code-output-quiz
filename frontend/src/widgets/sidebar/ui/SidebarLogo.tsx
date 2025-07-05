import { useNavigate } from "react-router-dom";
import LogoIcon from "../assets/icons/logo.svg";

export const SidebarLogo = () => {
  const navigate = useNavigate();

  return (
    <div className="inline-flex size-16 items-center justify-center">
      <button
        type="button"
        onClick={() => navigate("/")}
        className="grid size-10 place-content-center rounded-lg bg-gray-100 text-xs text-gray-600 cursor-pointer"
      >
        <img
          src={LogoIcon}
          alt="Code Output Quiz"
        />
      </button>
    </div>
  );
};
