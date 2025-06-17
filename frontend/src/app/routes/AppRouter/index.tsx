import { createBrowserRouter, RouterProvider } from 'react-router-dom';

import { HomePage } from '@/pages/home';

const router = createBrowserRouter([
  {
    path: '/',
    element: <HomePage />
  }
]);

export const AppRouter = () => {
  return <RouterProvider router={router} />;
};
