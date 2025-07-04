import { Menu } from "antd";
import { useSupportedProgrammingLanguages } from "@/features/select-programming-language";
import { getMenuItems, PROGRAMMING_KEY, USER_KEY } from "../config";

export const CustomMenu = () => {
  const { supportedProgrammingLanguages, loading } =
    useSupportedProgrammingLanguages();

  const items = getMenuItems(supportedProgrammingLanguages);

  if (loading) {
    return <div>Loading menu...</div>;
  }

  return (
    <Menu
      style={{ width: 256 }}
      defaultSelectedKeys={[
        supportedProgrammingLanguages[0]?.toLowerCase() ?? ""
      ]}
      defaultOpenKeys={[PROGRAMMING_KEY, USER_KEY]}
      mode="inline"
      items={items}
      className="min-h-[100vh] overflow-y-auto"
    />
  );
};
