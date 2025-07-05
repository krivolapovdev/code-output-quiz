import axios from "axios";
import { backendBaseUrl } from "../config";
import type { AuthResponse } from "./auth";

export const api = axios.create({
  baseURL: backendBaseUrl,
  timeout: 5000,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json"
  }
});

api.interceptors.request.use(config => {
  const accessToken = localStorage.getItem("accessToken");

  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }

  return config;
});

api.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      localStorage.removeItem("accessToken");

      originalRequest._retry = true;

      try {
        const res = await api.post<AuthResponse>("/api/v1/auth/refresh", {});

        const newAccessToken = res.data.accessToken;

        localStorage.setItem("accessToken", newAccessToken);

        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
        return api(originalRequest);
      } catch (refreshError) {
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
