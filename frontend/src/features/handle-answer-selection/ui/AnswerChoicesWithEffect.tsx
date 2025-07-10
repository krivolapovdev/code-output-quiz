import type { AnswerChoiceData } from "@/shared/api/quiz";
import { startConfettiFireworks } from "@/shared/lib/confetti";
import { useQuizStore } from "@/shared/lib/store";
import { useReportAnswer } from "../model/useReportAnswer";
import { AnswerChoices } from "./AnswerChoices";

type Props = {
  answerChoices: AnswerChoiceData[];
  onAnswerClick?: (answer: string) => void;
  selectedAnswer?: string | null;
  correctAnswer?: string;
  showFeedback?: boolean;
};

export const AnswerChoicesWithEffect = ({
  answerChoices,
  onAnswerClick,
  selectedAnswer,
  correctAnswer,
  showFeedback
}: Props) => {
  const { quiz } = useQuizStore();
  const reportAnswer = useReportAnswer();

  const handleSelect = (option: string) => {
    if (!quiz) {
      return;
    }

    if (quiz.correctAnswer === option) {
      startConfettiFireworks();
    }

    void reportAnswer(quiz.id, option);
    onAnswerClick?.(option);
  };

  return (
    <AnswerChoices
      answerChoiceData={answerChoices}
      onSelect={handleSelect}
      selectedAnswer={selectedAnswer}
      correctAnswer={correctAnswer}
      showFeedback={showFeedback}
    />
  );
};
