export type QuizResponse = {
  id: string;
  code: string;
  correctAnswer: string;
  explanation: string;
  programmingLanguage: string;
  difficultyLevel: string;
  answerChoices: AnswerChoiceData[];
};

export type AnswerChoiceData = {
  choice: "A" | "B" | "C" | "D";
  text: string;
};

export type ProgrammingLanguageResponse = {
  name: string;
  displayName: string;
};
