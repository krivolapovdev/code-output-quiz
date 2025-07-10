import type { AnswerChoiceData } from "@/shared/api/quiz";
import { startConfettiFireworks } from "@/shared/lib/confetti";
import { useQuizStore } from "@/shared/lib/store";
import { useReportAnswer } from "../model/useReportAnswer";
import { AnswerChoices } from "./AnswerChoices";

type Props = {
  answerChoices: AnswerChoiceData[];
};

export const AnswerChoicesWithEffect = ({ answerChoices }: Props) => {
  const { quiz } = useQuizStore();
  const reportAnswer = useReportAnswer();

  const handleSelect = (option: string) => {
    if (!quiz) {
      return;
    }

    if (quiz.correctAnswer === option) {
      startConfettiFireworks();
    }

    reportAnswer(quiz.id, option);
  };

  return (
    <AnswerChoices
      answerChoiceData={answerChoices}
      onSelect={handleSelect}
    />
  );
};
