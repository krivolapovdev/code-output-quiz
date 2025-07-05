import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { type LoginFormValues, loginSchema } from "./schema";

export const useLoginForm = () =>
  useForm<LoginFormValues>({
    resolver: zodResolver(loginSchema),
    mode: "onChange"
  });
