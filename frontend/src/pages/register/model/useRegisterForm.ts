import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import type { RegisterFormValues } from "./schema";
import { registerSchema } from "./schema";

export const useRegisterForm = () => {
  return useForm<RegisterFormValues>({
    resolver: zodResolver(registerSchema),
    mode: "onChange"
  });
};
