import { useState } from "react";
import { useRandomQuiz } from "@/features/fetch-random-quiz";
import { AnswerChoicesWithEffect } from "@/features/handle-answer-selection";
import { useUserStore } from "@/shared/lib/store";
import { CodeBlock } from "@/shared/ui/code-block";
import { prismLanguageResolver } from "../lib";
import { QuizResultModal } from "./QuizResultModal.tsx";
import { SelectDifficultyLevel } from "./SelectDifficultyLevel";
import { SelectProgrammingLanguage } from "./SelectProgrammingLanguage";

export const CodeQuizCard = () => {
  const { quiz, loading, error, refetch } = useRandomQuiz();
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
    <div className="m-8 flex w-full flex-col rounded-lg border border-[#e6f4ff]">
      <div className="mb-4 flex justify-end gap-x-4">
        <SelectProgrammingLanguage />
        <SelectDifficultyLevel />
      </div>

      <CodeBlock
        code={quiz.code}
        language={prismLanguageResolver.resolve(quiz.programmingLanguage)}
      />

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
