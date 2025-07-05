import { api } from "../base";
import type { AuthRequest, AuthResponse } from "./auth";

const baseURL = "/api/v1/auth";

export const authService = {
  register: async (payload: AuthRequest): Promise<AuthResponse> => {
    const { data } = await api.post<AuthResponse>(
      `${baseURL}/register`,
      payload
    );
    return data;
  },

  login: async (payload: AuthRequest): Promise<AuthResponse> => {
    const { data } = await api.post<AuthResponse>(`${baseURL}/login`, payload);
    return data;
  }
};
