import { useNavigate } from "react-router-dom";
import {
  type RegisterFormValues,
  useRegisterForm,
  useRegisterMutation
} from "../model";
import { RegisterField } from "./RegisterField";
import { SubmitButton } from "./SubmitButton";

export const RegisterForm = () => {
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors, isValid }
  } = useRegisterForm();

  const { mutateAsync } = useRegisterMutation();

  const onSubmit = async (data: RegisterFormValues) => {
    await mutateAsync(data);
    navigate("/");
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto lg:py-0">
        <div className="w-full bg-white rounded-lg shadow border sm:max-w-md xl:p-0 md:min-w-md">
          <div className="p-6 space-y-4 sm:p-8">
            <p className="text-xl font-bold text-gray-900">Create an account</p>

            <RegisterField
              label="Email"
              type="text"
              placeholder="you@example.com"
              {...register("email")}
              error={errors.email?.message}
            />

            <RegisterField
              label="Password"
              type="password"
              placeholder="••••••••"
              {...register("password")}
              error={errors.password?.message}
            />

            <RegisterField
              label="Confirm Password"
              type="password"
              placeholder="••••••••"
              {...register("confirmPassword")}
              error={errors.confirmPassword?.message}
            />

            <SubmitButton
              label="Create an account"
              disabled={!isValid}
            />
          </div>
        </div>
      </div>
    </form>
  );
};
