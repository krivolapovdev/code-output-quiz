export type QuizResponse = {
  id: string;
  code: string;
  correctAnswer: string;
  explanation: string;
  programmingLanguage: string;
  difficultyLevel: string;
};

export type ProgrammingLanguageResponse = {
  name: string;
  displayName: string;
};
