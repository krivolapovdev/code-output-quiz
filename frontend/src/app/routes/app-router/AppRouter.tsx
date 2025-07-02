import {
  createBrowserRouter,
  Navigate,
  RouterProvider
} from "react-router-dom";

import { HomePage } from "@/pages/home";
import { LoginPage } from "@/pages/login";
import { RegisterPage } from "@/pages/register";

const router = createBrowserRouter([
  {
    path: "/",
    element: <HomePage />
  },
  {
    path: "/register",
    element: <RegisterPage />
  },
  {
    path: "/login",
    element: <LoginPage />
  },
  {
    path: "*",
    element: (
      <Navigate
        to="/"
        replace
      />
    )
  }
]);

export const AppRouter = () => {
  return <RouterProvider router={router} />;
};
