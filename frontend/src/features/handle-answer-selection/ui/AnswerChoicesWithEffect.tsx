import { AnswerChoices } from "@/entities/answer-choices";
import { startConfettiFireworks } from "@/shared/lib/confetti";

type Props = {
  options: string[];
  onSelect: (option: string) => void;
};

export const AnswerChoicesWithEffect = ({ options, onSelect }: Props) => {
  const handleSelect = (option: string) => {
    startConfettiFireworks();
    onSelect(option);
  };

  return (
    <AnswerChoices
      options={options}
      onSelect={handleSelect}
    />
  );
};
