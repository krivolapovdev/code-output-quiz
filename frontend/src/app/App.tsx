import "@/app/styles/index.css";

import { ErrorBoundaryProvider, ReactQueryProvider } from "@/app/providers";
import { AppRouter } from "@/app/routes";
import { initAuth } from "@/shared/lib/auth/initAuth";

initAuth();

export const App = () => {
  return (
    <ErrorBoundaryProvider>
      <ReactQueryProvider>
        <AppRouter />
      </ReactQueryProvider>
    </ErrorBoundaryProvider>
  );
};
