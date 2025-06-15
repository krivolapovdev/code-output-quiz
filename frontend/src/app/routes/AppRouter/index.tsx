import { createHashRouter, RouterProvider } from 'react-router-dom';

import { HomePage } from '@/pages/home';

const router = createHashRouter([
  {
    path: '/',
    element: <HomePage />
  }
]);

export const AppRouter = () => {
  return <RouterProvider router={router} />;
};
