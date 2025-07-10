import { Modal } from "antd";
import type { AnswerChoiceData } from "@/shared/api/quiz";

type Props = {
  open: boolean;
  onClose: () => void;
  selected: AnswerChoiceData | undefined;
  correct: AnswerChoiceData | undefined;
  explanation: string;
  showExplanation: boolean;
};

export const QuizResultModal = ({
  open,
  onClose,
  selected,
  correct,
  explanation,
  showExplanation
}: Props) => {
  const isCorrect = selected?.choice === correct?.choice;

  return (
    <Modal
      open={open}
      onCancel={onClose}
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

        <div className="flex justify-end pt-2">
          <button
            type="button"
            onClick={onClose}
            className="rounded bg-blue-600 px-5 py-2 text-white hover:bg-blue-700 transition cursor-pointer"
          >
            Next
          </button>
        </div>
      </div>
    </Modal>
  );
};
