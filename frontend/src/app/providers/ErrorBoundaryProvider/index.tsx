import { ErrorBoundary } from 'react-error-boundary';

type ErrorBoundaryProviderProps = {
  children: React.ReactNode;
};

export const ErrorBoundaryProvider = ({ children }: ErrorBoundaryProviderProps) => {
  return <ErrorBoundary fallback={<div>Something went wrong...</div>}>{children}</ErrorBoundary>;
};
