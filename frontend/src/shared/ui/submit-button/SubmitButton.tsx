type SubmitButtonProps = {
  label: string;
  disabled?: boolean;
};

export const SubmitButton = ({ label, disabled }: SubmitButtonProps) => {
  return (
    <button
      type="submit"
      disabled={disabled}
      className={`w-full text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center
          ${disabled ? "opacity-50 cursor-not-allowed" : ""}`}
    >
      {label}
    </button>
  );
};
