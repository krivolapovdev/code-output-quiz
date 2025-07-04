import LogoIcon from "../assets/icons/logo.svg";

export const SidebarLogo = () => {
  return (
    <div className="inline-flex size-16 items-center justify-center">
      <a
        href="/"
        className="grid size-10 place-content-center rounded-lg bg-gray-100 text-xs text-gray-600"
      >
        <img
          src={LogoIcon}
          alt="Code Output Quiz"
        />
      </a>
    </div>
  );
};
