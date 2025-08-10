import type { AnswerChoiceData } from "@/shared/api";

type Props = {
  answerChoiceData: AnswerChoiceData[];
  onSelect: (option: string) => void;
  selectedAnswer?: string | null;
  correctAnswer?: string;
  showFeedback?: boolean;
};

const labelColors = [
  "text-green-600",
  "text-blue-600",
  "text-yellow-600",
  "text-purple-600"
];

export const AnswerChoices = ({
  answerChoiceData,
  onSelect,
  selectedAnswer,
  correctAnswer,
  showFeedback
}: Props) => {
  return (
    <div className="py-6 grid grid-cols-1 md:grid-cols-2 gap-4">
      {answerChoiceData.map((data, index) => {
        const label = data.choice;
        const isCorrect = label === correctAnswer;
        const isSelected = label === selectedAnswer;

        let borderColor = "border-transparent";

        if (showFeedback) {
          if (isCorrect) {
            borderColor = "border-green-500";
          } else if (isSelected) {
            borderColor = "border-yellow-500";
          } else {
            borderColor = "border-gray-200";
          }
        }

        return (
          <button
            key={data.text}
            type="button"
            onClick={() => onSelect(label)}
            className={`flex cursor-pointer items-start gap-2 rounded-md border ${borderColor} bg-white px-4 py-2 text-left text-black transition-all duration-200 hover:border-blue-500`}
          >
            <span
              className={`font-semibold ${labelColors[index % labelColors.length]}`}
            >
              {label})
            </span>{" "}
            <span>{data.text}</span>
          </button>
        );
      })}
    </div>
  );
};
