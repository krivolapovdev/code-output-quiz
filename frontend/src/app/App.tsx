import "@/app/styles/index.css";

import { ErrorBoundaryProvider } from "@/app/providers/error-boundary";
import { ReactQueryProvider } from "@/app/providers/react-query";
import { AppRouter } from "@/app/routes/app-router/AppRouter";

export const App = () => {
  return (
    <ErrorBoundaryProvider>
      <ReactQueryProvider>
        <AppRouter />
      </ReactQueryProvider>
    </ErrorBoundaryProvider>
  );
};
