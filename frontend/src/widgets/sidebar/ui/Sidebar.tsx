import { SidebarFooter } from "./SidebarFooter";
import { SidebarLogo } from "./SidebarLogo";
import { SidebarNavSection } from "./SidebarNavSection";

export const Sidebar = () => {
  return (
    <div className="flex h-screen w-16 flex-col justify-between border-e border-gray-100 bg-white z-10">
      <div>
        <SidebarLogo />
        <SidebarNavSection />
      </div>
      <SidebarFooter />
    </div>
  );
};
