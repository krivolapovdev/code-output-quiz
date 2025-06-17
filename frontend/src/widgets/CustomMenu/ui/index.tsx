import { Menu } from 'antd';

import { menuItems } from '../config';
import { DIFFICULTY_KEY, JAVA_KEY, LANGUAGE_KEY, PROGRAMMING_KEY } from '../consts';

export const CustomMenu = () => {
  return (
    <Menu
      style={{ width: 256 }}
      defaultSelectedKeys={JAVA_KEY}
      defaultOpenKeys={[PROGRAMMING_KEY, LANGUAGE_KEY, DIFFICULTY_KEY]}
      mode='inline'
      items={menuItems}
      className='min-h-[100vh] overflow-y-auto'
    />
  );
};
