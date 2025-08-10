import { CodeOutputQuiz } from "@/widgets/code-quiz-card";
import { Sidebar } from "@/widgets/sidebar";

export const HomePage = () => {
  return (
    <div className="h-screen">
      <Sidebar />
      <div className="ml-16 h-full">
        <CodeOutputQuiz />
      </div>
    </div>
  );
};
