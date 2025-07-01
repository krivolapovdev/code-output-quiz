import { forwardRef, useId } from "react";

type Props = {
  label: string;
  type?: string;
  placeholder?: string;
  error?: string;
} & React.InputHTMLAttributes<HTMLInputElement>;

export const RegisterField = forwardRef<HTMLInputElement, Props>(
  ({ label, type = "text", placeholder, error, ...props }, ref) => {
    const id = useId();

    return (
      <div>
        <label
          htmlFor={id}
          className="block mb-2 text-sm font-medium text-gray-900"
        >
          {label}
        </label>
        <input
          id={id}
          type={type}
          ref={ref}
          placeholder={placeholder}
          className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg block w-full p-2.5"
          {...props}
        />
        {error && <p className="text-red-500 text-sm mt-1">{error}</p>}
      </div>
    );
  }
);

RegisterField.displayName = "RegisterField";
