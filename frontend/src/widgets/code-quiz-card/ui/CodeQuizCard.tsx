import { useState } from "react";
import { useNextQuiz } from "@/features/fetch-next-quiz";
import { useUserStore } from "@/shared/lib/store";
import { CodeBlock } from "@/shared/ui/code-block";
import { prismLanguageResolver } from "../lib";
import { AnswerChoicesWithEffect } from "./AnswerChoicesWithEffect";
import { QuizResultModal } from "./QuizResultModal";
import { SelectDifficultyLevel } from "./SelectDifficultyLevel";
import { SelectProgrammingLanguage } from "./SelectProgrammingLanguage";

export const CodeQuizCard = () => {
  const { quiz, loading, error, refetch } = useNextQuiz();
  const { user } = useUserStore();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedAnswer, setSelectedAnswer] = useState<string | null>(null);

  if (loading) {
    return <div className="p-8 text-black">Loading quiz...</div>;
  }

  if (error) {
    return <div className="p-8 text-red-500">Error: {error}</div>;
  }

  if (!quiz) {
    return null;
  }

  const correct = quiz.answerChoices.find(a => a.choice === quiz.correctAnswer);
  const selected = quiz.answerChoices.find(a => a.choice === selectedAnswer);

  const handleAnswerClick = (answer: string) => {
    setSelectedAnswer(answer);
    setIsModalOpen(true);
  };

  const handleNext = () => {
    setIsModalOpen(false);
    setSelectedAnswer(null);
    void refetch();
  };

  return (
    <div className="pt-8 px-8 flex w-full h-full flex-col rounded-lg border border-[#e6f4ff]">
      <div className="mb-4 flex justify-end gap-x-4">
        <SelectProgrammingLanguage />
        <SelectDifficultyLevel />
      </div>

      <div className="flex-1 flex flex-col">
        <CodeBlock
          code={quiz.code}
          language={prismLanguageResolver.resolve(quiz.programmingLanguage)}
        />
      </div>

      <AnswerChoicesWithEffect
        answerChoices={quiz.answerChoices}
        onAnswerClick={handleAnswerClick}
        selectedAnswer={selectedAnswer}
        correctAnswer={quiz.correctAnswer}
        showFeedback={isModalOpen}
      />

      <QuizResultModal
        open={isModalOpen}
        callNext={handleNext}
        selected={selected}
        correct={correct}
        explanation={quiz.explanation}
        showExplanation={!!user}
      />
    </div>
  );
};
