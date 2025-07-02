import "@/app/styles/index.css";

import { ErrorBoundaryProvider, ReactQueryProvider } from "@/app/providers";
import { AppRouter } from "@/app/routes";

export const App = () => {
  return (
    <ErrorBoundaryProvider>
      <ReactQueryProvider>
        <AppRouter />
      </ReactQueryProvider>
    </ErrorBoundaryProvider>
  );
};
