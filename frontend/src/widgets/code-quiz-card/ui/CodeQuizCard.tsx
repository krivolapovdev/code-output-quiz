import { useRandomQuiz } from "@/features/fetch-random-quiz";
import { AnswerChoicesWithEffect } from "@/features/handle-answer-selection";
import { CodeBlock } from "@/shared/ui/code-block";
import { SelectDifficultyLevel } from "./SelectDifficultyLevel";
import { SelectProgrammingLanguage } from "./SelectProgrammingLanguage";

export const CodeQuizCard = () => {
  const { quiz, loading, error } = useRandomQuiz();

  if (loading) {
    return <div className="p-8 text-black">Loading quiz...</div>;
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
