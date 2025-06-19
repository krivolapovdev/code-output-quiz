import { AnswerChoices } from "@/entities/answer-choices";
import { CustomTabs } from "@/entities/custom-tabs";
import { CodeBlock } from "@/shared/ui/code-block";

const text = `let x = 5;
let y = "5";

console.log(x == y);
`;

export const CodeOutputQuiz = () => {
  return (
    <div className="m-8 flex w-full flex-col rounded-lg border-1 border-[#e6f4ff]">
      <CustomTabs />
      <CodeBlock
        text={text}
        language="javascript"
      />
      <AnswerChoices
        options={["true", "false", "undefined", "TypeError"]}
        onSelect={e => console.log(e)}
      />
    </div>
  );
};
