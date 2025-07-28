import axios from "axios";
import { backendBaseUrl } from "@/shared/config";
import { useUserStore } from "@/shared/lib/store";

export const api = axios.create({
  baseURL: backendBaseUrl,
  timeout: 10_000,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json"
  }
});

api.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        await api.post("/api/v1/auth/refresh", {});
        return api(originalRequest);
      } catch (refreshError) {
        useUserStore.getState().setUser(null);
        window.location.href = "/login";

        return Promise.reject(
          refreshError instanceof Error
            ? refreshError
            : new Error(JSON.stringify(refreshError))
        );
      }
    }

    return Promise.reject(
      error instanceof Error ? error : new Error(JSON.stringify(error))
    );
  }
);
