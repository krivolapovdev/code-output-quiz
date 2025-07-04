import { CodeOutputQuiz } from "@/widgets/code-quiz-card";
import { CustomMenu } from "@/widgets/custom-menu";

export const HomePage = () => {
  return (
    <div className="flex">
      <CustomMenu />
      <CodeOutputQuiz />
    </div>
  );
};
