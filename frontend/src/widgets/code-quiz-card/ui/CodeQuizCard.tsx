import { useRandomQuiz } from "@/features/fetch-random-quiz";
import { AnswerChoicesWithEffect } from "@/features/handle-answer-selection/ui/AnswerChoicesWithEffect";
import { DifficultyTabs } from "@/features/select-difficulty-level";
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
      <DifficultyTabs />
      <CodeBlock
        code={quiz.code}
        language={quiz.programmingLanguage.toLowerCase()}
      />
      <AnswerChoicesWithEffect options={options} />
    </div>
  );
};
