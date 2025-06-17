import { CustomCodeBlock } from '@/entities/custom-code-block';
import { CustomMenu } from '@/widgets/custom-menu';

const text = `const arr = [1, 2, 3];
const max = Math.max(...arr);`;

export const HomePage = () => {
  return (
    <div className='flex'>
      <CustomMenu />
      <CustomCodeBlock text={text} language={'javascript'} />
    </div>
  );
};
