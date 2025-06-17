import { CodeBlock, dracula } from 'react-code-blocks';

type CustomCodeBlockProps = {
  text: string;
  language: string;
};

export const CustomCodeBlock = ({ text, language }: CustomCodeBlockProps) => {
  return (
    <div className='w-full'>
      <CodeBlock text={text} language={language} showLineNumbers={true} theme={dracula} />
    </div>
  );
};
