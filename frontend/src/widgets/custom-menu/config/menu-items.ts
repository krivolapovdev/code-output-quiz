import { CodeOutlined, UserOutlined } from '@ant-design/icons';
import type { MenuProps } from 'antd';
import { createElement } from 'react';

import { GO_KEY, JAVA_KEY, PROGRAMMING_KEY, SETTINGS_KEY, SOLVED_KEY, TYPESCRIPT_KEY, USER_KEY } from './';

export const menuItems: Required<MenuProps>['items'][number][] = [
  {
    key: PROGRAMMING_KEY,
    label: 'Programming',
    icon: createElement(CodeOutlined),
    children: [
      { key: JAVA_KEY, label: 'Java' },
      { key: TYPESCRIPT_KEY, label: 'Typescript' },
      { key: GO_KEY, label: 'Go' }
    ]
  },
  {
    type: 'divider'
  },
  {
    key: USER_KEY,
    label: 'User',
    icon: createElement(UserOutlined),
    children: [
      { key: SOLVED_KEY, label: 'Solved' },
      { key: SETTINGS_KEY, label: 'Settings' }
    ]
  }
];
