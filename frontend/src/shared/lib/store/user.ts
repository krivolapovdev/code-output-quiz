import { create } from "zustand";
import type { User } from "@/shared/api/user";

type UserStore = {
  user: User | null;
  setUser: (u: User) => void;
};

export const useUserStore = create<UserStore>(set => ({
  user: null,
  setUser: user => set({ user })
}));
