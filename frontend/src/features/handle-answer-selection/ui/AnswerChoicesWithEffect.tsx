import { startConfettiFireworks } from "@/shared/lib/confetti";
import { useQuizStore } from "@/shared/lib/store";
import { useReportAnswer } from "../model/useReportAnswer";
import { AnswerChoices } from "./AnswerChoices";

type Props = {
  options: string[];
};

export const AnswerChoicesWithEffect = ({ options }: Props) => {
  const { quiz } = useQuizStore();
  const reportAnswer = useReportAnswer();

  const handleSelect = (option: string) => {
    console.log(`1: ${option}`);

    if (quiz) {
      console.log(`2: ${option}`);
      if (quiz.correctAnswer === option) {
        startConfettiFireworks();
      }
      reportAnswer(quiz.id, option);
    }
  };

  return (
    <AnswerChoices
      options={options}
      onSelect={handleSelect}
    />
  );
};
