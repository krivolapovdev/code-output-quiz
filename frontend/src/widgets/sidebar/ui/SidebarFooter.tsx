import { useUserStore } from "@/shared/lib/store";
import LogoutIcon from "../assets/icons/logout.svg";
import { useLogout } from "../model";

export const SidebarFooter = () => {
  const { user } = useUserStore();
  const logout = useLogout();

  if (!user) {
    return null;
  }

  return (
    <div className="sticky inset-x-0 bottom-0 border-t border-gray-100 bg-white p-2">
      <button
        type="button"
        onClick={logout}
        className="group relative flex w-full justify-center rounded-lg px-2 py-1.5 text-sm text-gray-500 hover:bg-gray-50 hover:text-gray-700"
      >
        <div className="size-5 opacity-75">
          <img
            src={LogoutIcon}
            alt=""
          />
        </div>
        <span className="invisible absolute start-full top-1/2 ms-4 -translate-y-1/2 rounded-sm bg-gray-900 px-2 py-1.5 text-xs font-medium text-white group-hover:visible">
          Logout
        </span>
      </button>
    </div>
  );
};
