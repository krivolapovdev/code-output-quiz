type SubmitButtonProps = {
  label: string;
  disabled?: boolean;
};

export const SubmitButton = ({
  label,
  disabled = false
}: SubmitButtonProps) => {
  return (
    <button
      type="submit"
      disabled={disabled}
      className={`w-full font-medium rounded-lg text-sm px-5 py-2.5 text-center text-white transition
        ${
          disabled
            ? "bg-gray-400 cursor-not-allowed"
            : "bg-blue-500 hover:bg-blue-700 focus:outline-none focus:ring-4 focus:ring-blue-300"
        }`}
    >
      {label}
    </button>
  );
};
