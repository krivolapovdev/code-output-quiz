import GithubIcon from "../assets/icons/github.svg";
import User from "../assets/icons/user.svg";
import { SidebarNavItem } from "./SidebarNavItem";

const navItems = [
  {
    label: "Login",
    icon: User,
    href: "/login",
    alt: "Account icon"
  },
  {
    label: "Github",
    icon: GithubIcon,
    external: true,
    href: "https://github.com/krivolapovdev/code-output-quiz",
    alt: "GitHub icon"
  }
];

export const SidebarNavSection = () => {
  return (
    <div className="border-t border-gray-100">
      <ul className="space-y-4 border-t border-gray-100 pt-4">
        {navItems.map(({ icon, alt, ...item }) => (
          <li key={item.label}>
            <SidebarNavItem
              {...item}
              icon={
                <img
                  src={icon}
                  alt={alt}
                />
              }
            />
          </li>
        ))}
      </ul>
    </div>
  );
};
