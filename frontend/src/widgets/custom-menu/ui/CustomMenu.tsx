import { Menu } from "antd";

import {
  DIFFICULTY_KEY,
  JAVA_KEY,
  LANGUAGE_KEY,
  menuItems,
  PROGRAMMING_KEY,
  USER_KEY
} from "../config";

export const CustomMenu = () => {
  return (
    <Menu
      style={{ width: 256 }}
      defaultSelectedKeys={[JAVA_KEY]}
      defaultOpenKeys={[
        PROGRAMMING_KEY,
        LANGUAGE_KEY,
        DIFFICULTY_KEY,
        USER_KEY
      ]}
      mode="inline"
      items={menuItems}
      className="min-h-[100vh] overflow-y-auto"
    />
  );
};
