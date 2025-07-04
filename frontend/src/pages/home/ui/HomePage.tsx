import { CodeOutputQuiz } from "@/widgets/code-quiz-card";
import { Sidebar } from "@/widgets/sidebar";

export const HomePage = () => {
  return (
    <div className="flex">
      <Sidebar />
      <CodeOutputQuiz />
    </div>
  );
};
