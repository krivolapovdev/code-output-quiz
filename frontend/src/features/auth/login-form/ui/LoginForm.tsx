import { Link, useNavigate } from "react-router-dom";
import { AuthField } from "@/shared/ui/auth-field";
import { SubmitButton } from "@/shared/ui/submit-button";
import { useLoginForm } from "../model/useLoginForm";
import { useLoginMutation } from "../model/useLoginMutation";

export const LoginForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors, isValid }
  } = useLoginForm();

  const { mutateAsync, isPending } = useLoginMutation();
  const navigate = useNavigate();

  const onSubmit = async (data: { email: string; password: string }) => {
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
          <h1 className="text-2xl font-semibold text-gray-900">Log in</h1>
          <p className="text-sm text-gray-500 mt-1">Access your account</p>
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

        <SubmitButton
          label={isPending ? "Logging in..." : "Log in"}
          disabled={!isValid || isPending}
        />

        <p className="text-sm text-center text-gray-500">
          Don’t have an account?{" "}
          <Link
            to="/register"
            className="text-blue-600 hover:underline"
          >
            Register
          </Link>
        </p>
      </div>
    </form>
  );
};
