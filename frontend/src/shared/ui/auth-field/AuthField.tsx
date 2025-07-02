import { forwardRef, useId } from "react";

type AuthFieldProps = {
  label: string;
  type: "text" | "password" | "email";
  placeholder?: string;
  error?: string;
};

export const AuthField = forwardRef<HTMLInputElement, AuthFieldProps>(
  ({ label, type, placeholder, error, ...rest }, ref) => {
    const id = useId();

    return (
      <div className="space-y-1">
        <label
          htmlFor={id}
          className="block text-sm font-medium text-gray-700"
        >
          {label}
        </label>
        <input
          id={id}
          ref={ref}
          type={type}
          placeholder={placeholder}
          className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg block w-full p-2.5 focus:outline-none focus:ring-2 focus:ring-blue-500"
          {...rest}
        />
        {error && <p className="text-sm text-red-500">{error}</p>}
      </div>
    );
  }
);

AuthField.displayName = "AuthField";
