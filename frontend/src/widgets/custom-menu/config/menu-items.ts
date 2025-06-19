import { PROGRAMMING_KEY, USER_KEY } from "./menu-keys";

export const getMenuItems = (languages: string[]) => [
  {
    key: PROGRAMMING_KEY,
    label: "Programming",
    children: languages.map(lang => ({
      key: lang.toLowerCase(),
      label: lang
    }))
  },
  {
    key: USER_KEY,
    label: "User",
    children: [
      { key: "profile", label: "Profile" },
      { key: "settings", label: "Settings" }
    ]
  }
];
