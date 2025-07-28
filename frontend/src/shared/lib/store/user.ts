import { create } from "zustand";
import type { User } from "@/shared/api";

type UserStore = {
  user: User | null;
  setUser: (user: User | null) => void;
};

export const useUserStore = create<UserStore>(set => ({
  user: null,
  setUser: user => set({ user })
}));
