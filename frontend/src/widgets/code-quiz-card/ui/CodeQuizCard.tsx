import { useRandomQuiz } from "@/features/fetch-random-quiz";
import { AnswerChoicesWithEffect } from "@/features/handle-answer-selection/ui/AnswerChoicesWithEffect";
import { SelectDifficultyLevel } from "@/features/select-difficulty-level/ui/SelectDifficultyLevel";
import { SelectProgrammingLanguage } from "@/features/select-programming-language/ui";
import { CodeBlock } from "@/shared/ui/code-block";

export const CodeQuizCard = () => {
  const { quiz, loading, error } = useRandomQuiz();

  if (loading) {
    return <div className="p-8">Loading quiz...</div>;
  }

  if (error) {
    return <div className="p-8 text-red-500">Error: {error}</div>;
  }

  if (!quiz) {
    return null;
  }

  const options = ["true", "false", "undefined", "temporary"];

  return (
    <div className="m-8 flex w-full flex-col rounded-lg border-1 border-[#e6f4ff]">
      <div className="mb-4 flex flex-row gap-x-4 justify-end">
        <SelectProgrammingLanguage />
        <SelectDifficultyLevel />
      </div>
      <CodeBlock
        code={quiz.code}
        language={quiz.programmingLanguage.toLowerCase()}
      />
      <AnswerChoicesWithEffect options={options} />
    </div>
  );
};
