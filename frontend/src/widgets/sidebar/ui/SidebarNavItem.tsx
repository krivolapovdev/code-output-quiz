type Props = {
  label: string;
  icon: React.ReactNode;
  href: string;
  active?: boolean;
  external?: boolean;
};

export const SidebarNavItem = ({
  label,
  icon,
  href,
  active = false,
  external = false
}: Props) => {
  return (
    <a
      href={href}
      target={external ? "_blank" : undefined}
      className={`group relative flex justify-center rounded-sm px-2 py-1.5 ${
        active
          ? "bg-blue-50 text-blue-700"
          : "text-gray-500 hover:bg-gray-50 hover:text-gray-700"
      }`}
    >
      <div className="size-5 opacity-75">{icon}</div>
      <span className="invisible absolute start-full top-1/2 ms-4 -translate-y-1/2 rounded-sm bg-gray-900 px-2 py-1.5 text-xs font-medium text-white group-hover:visible">
        {label}
      </span>
    </a>
  );
};
