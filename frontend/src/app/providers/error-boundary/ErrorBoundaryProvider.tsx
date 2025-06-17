import { ErrorBoundary } from 'react-error-boundary';

import { ErrorFallback } from '@/shared/ui/error-fallback';

type ErrorBoundaryProviderProps = {
  children: React.ReactNode;
};

export const ErrorBoundaryProvider = ({
  children,
}: ErrorBoundaryProviderProps) => {
  return (
    <ErrorBoundary fallbackRender={ErrorFallback}>{children}</ErrorBoundary>
  );
};
