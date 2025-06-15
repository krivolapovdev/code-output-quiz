import { ErrorBoundary } from 'shared/ui/ErrorBoundary';

export const ErrorBoundaryProvider = ({ children }: { children: React.ReactNode }) => {
  return <ErrorBoundary>{children}</ErrorBoundary>;
};
