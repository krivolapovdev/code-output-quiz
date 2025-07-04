import axios from "axios";
import { backendBaseUrl } from "../config";

export const api = axios.create({
  baseURL: backendBaseUrl,
  timeout: 5000,
  headers: {
    "Content-Type": "application/json"
  }
});
