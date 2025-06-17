import "@/app/styles/index.css";

import { ErrorBoundaryProvider } from "@/app/providers/error-boundary/ErrorBoundaryProvider";
import { AppRouter } from "@/app/routes/app-router/AppRouter";

export const App = () => {
  return (
    <ErrorBoundaryProvider>
      <AppRouter />
    </ErrorBoundaryProvider>
  );
};
