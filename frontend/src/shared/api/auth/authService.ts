import { api } from "../base";
import type { AuthRequest } from "./types.ts";

const baseURL = "/api/v1/auth";

export const authService = {
  register: async (payload: AuthRequest): Promise<void> => {
    await api.post(`${baseURL}/register`, payload);
  },

  login: async (payload: AuthRequest): Promise<void> => {
    await api.post(`${baseURL}/login`, payload);
  },

  logout: async (): Promise<void> => {
    await api.post(`${baseURL}/logout`);
  }
};
