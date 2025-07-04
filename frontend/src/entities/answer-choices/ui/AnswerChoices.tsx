type Props = {
  options: string[];
  onSelect: (option: string) => void;
};

const labelColors = [
  "text-green-600",
  "text-blue-600",
  "text-yellow-600",
  "text-purple-600"
];

export const AnswerChoices = ({ options, onSelect }: Props) => {
  return (
    <div className="mt-6 grid grid-cols-2 gap-4">
      {options.map((option, index) => {
        const label = String.fromCharCode("A".charCodeAt(0) + index);
        return (
          <button
            key={option}
            type="button"
            onClick={() => onSelect(label)}
            className="flex cursor-pointer items-start gap-2 rounded-md border border-transparent bg-white px-4 py-2 text-left text-black transition-all duration-200 hover:border-blue-500"
          >
            <span
              className={`font-semibold ${labelColors[index % labelColors.length]}`}
            >
              {label})
            </span>{" "}
            <span>{option}</span>
          </button>
        );
      })}
    </div>
  );
};
