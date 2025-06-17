import { CodeBlock, dracula } from 'react-code-blocks';

type CustomCodeBlockProps = {
  text: string;
  language: string;
};

export const CustomCodeBlocks = ({ text, language }: CustomCodeBlockProps) => {
  return (
    <div className='h-full'>
      <CodeBlock
        customStyle={{ height: '100%' }}
        text={text}
        language={language}
        showLineNumbers={true}
        theme={dracula}
      />
    </div>
  );
};
