import '@/app/styles/index.css';

import { ErrorBoundaryProvider } from '@/app/providers/ErrorBoundaryProvider';
import { AppRouter } from '@/app/routes/AppRouter';

export const App = () => {
  return (
    <ErrorBoundaryProvider>
      <AppRouter />
    </ErrorBoundaryProvider>
  );
};
