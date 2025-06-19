import { Menu } from "antd";
import { useSupportedProgrammingLanguages } from "@/entities/quizzes";
import { getMenuItems, PROGRAMMING_KEY, USER_KEY } from "../config";

export const CustomMenu = () => {
  const { languages, loading } = useSupportedProgrammingLanguages();

  const items = getMenuItems(languages);

  if (loading) {
    return <div>Loading menu...</div>;
  }

  return (
    <Menu
      style={{ width: 256 }}
      defaultSelectedKeys={[languages[0]?.toLowerCase() ?? ""]}
      defaultOpenKeys={[PROGRAMMING_KEY, USER_KEY]}
      mode="inline"
      items={items}
      className="min-h-[100vh] overflow-y-auto"
    />
  );
};
