import { CodeOutputQuiz } from '@/widgets/code-output-quiz';
import { CustomMenu } from '@/widgets/custom-menu';

export const HomePage = () => {
  return (
    <div className='flex'>
      <CustomMenu />
      <CodeOutputQuiz />
    </div>
  );
};
