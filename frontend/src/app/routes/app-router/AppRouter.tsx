import {
  createBrowserRouter,
  Navigate,
  RouterProvider
} from "react-router-dom";

import { HomePage } from "@/pages/home";
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
