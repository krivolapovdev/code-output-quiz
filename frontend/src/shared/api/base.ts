import axios from "axios";

export const api = axios.create({
  baseURL: "http://localhost:8765/api",
  timeout: 5000,
  headers: {
    "Content-Type": "application/json"
  }
});
