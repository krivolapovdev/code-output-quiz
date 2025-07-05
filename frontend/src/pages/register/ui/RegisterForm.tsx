import { Link, useNavigate } from "react-router-dom";
import { AuthField } from "@/shared/ui/auth-field";
import { SubmitButton } from "@/shared/ui/submit-button";
import type { RegisterFormValues } from "../model/schema";
import { useRegisterForm } from "../model/useRegisterForm";
import { useRegisterMutation } from "../model/useRegisterMutation";

export const RegisterForm = () => {
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors, isValid }
  } = useRegisterForm();

  const { mutateAsync, isPending } = useRegisterMutation();

  const onSubmit = async (data: RegisterFormValues) => {
    await mutateAsync(data);
    navigate("/");
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="min-h-screen flex items-center justify-center bg-gray-50 px-4"
    >
      <div className="w-full max-w-md bg-white rounded-2xl shadow-xl border border-gray-200 p-8 space-y-6">
        <div className="text-center">
          <h1 className="text-2xl font-semibold text-gray-900">
            Create account
          </h1>
          <p className="text-sm text-gray-500 mt-1">Sign up to get started</p>
        </div>

        <AuthField
          label="Email"
          type="text"
          placeholder="you@example.com"
          {...register("email")}
          error={errors.email?.message}
        />

        <AuthField
          label="Password"
          type="password"
          placeholder="••••••••"
          {...register("password")}
          error={errors.password?.message}
        />

        <AuthField
          label="Confirm Password"
          type="password"
          placeholder="••••••••"
          {...register("confirmPassword")}
          error={errors.confirmPassword?.message}
        />

        <SubmitButton
          label={isPending ? "Creating account..." : "Sign up"}
          disabled={!isValid || isPending}
        />

        <p className="text-sm text-center text-gray-500">
          Already have an account?{" "}
          <Link
            to="/login"
            className="text-blue-600 hover:underline"
          >
            Log in
          </Link>
        </p>
      </div>
    </form>
  );
};
