import type { AnswerChoiceData } from "@/shared/api/quiz";

type Props = {
  answerChoiceData: AnswerChoiceData[];
  onSelect: (option: string) => void;
};

const labelColors = [
  "text-green-600",
  "text-blue-600",
  "text-yellow-600",
  "text-purple-600"
];

export const AnswerChoices = ({ answerChoiceData, onSelect }: Props) => {
  return (
    <div className="mt-6 grid grid-cols-2 gap-4">
      {answerChoiceData.map((data, index) => {
        const label = String.fromCharCode("A".charCodeAt(0) + index);
        return (
          <button
            key={data.text}
            type="button"
            onClick={() => onSelect(label)}
            className="flex cursor-pointer items-start gap-2 rounded-md border border-transparent bg-white px-4 py-2 text-left text-black transition-all duration-200 hover:border-blue-500"
          >
            <span
              className={`font-semibold ${labelColors[index % labelColors.length]}`}
            >
              {data.choice})
            </span>{" "}
            <span>{data.text}</span>
          </button>
        );
      })}
    </div>
  );
};
