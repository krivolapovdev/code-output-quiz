import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

const queryClient = new QueryClient();

type ReactQueryProviderProps = {
  children: React.ReactNode;
};

export const ReactQueryProvider = ({ children }: ReactQueryProviderProps) => {
  return (
    <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
  );
};
