import { Modal } from "antd";
import type { AnswerChoiceData } from "@/shared/api";
import { QuizResultModalButtons } from "./QuizResultModalButtons.tsx";

type Props = {
  open: boolean;
  callNext: () => void;
  selected: AnswerChoiceData | undefined;
  correct: AnswerChoiceData | undefined;
  explanation: string;
  showExplanation: boolean;
};

export const QuizResultModal = ({
  open,
  callNext,
  selected,
  correct,
  explanation,
  showExplanation
}: Props) => {
  const isCorrect = selected?.choice === correct?.choice;

  return (
    <Modal
      open={open}
      footer={null}
      closable={false}
      maskClosable={false}
      keyboard={false}
      centered
    >
      <div className="space-y-4">
        <p>
          <span className="text-gray-700 font-semibold">Your selected:</span>{" "}
          <span className={isCorrect ? "text-green-600" : "text-yellow-600"}>
            {selected?.choice}) {selected?.text}
          </span>
        </p>

        <p>
          <span className="text-gray-700 font-semibold">Correct answer:</span>{" "}
          <span className="text-green-600">
            {correct?.choice}) {correct?.text}
          </span>
        </p>

        <p className="text-gray-700">
          <span className="font-semibold">Explanation:</span>{" "}
          {showExplanation ? explanation : "Only for logged in users"}
        </p>

        <QuizResultModalButtons
          onLike={callNext}
          onDislike={callNext}
          onNext={callNext}
        />
      </div>
    </Modal>
  );
};
