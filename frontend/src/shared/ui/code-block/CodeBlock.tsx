import Prism from "prismjs";
import { useEffect, useRef } from "react";
import "prismjs/plugins/line-numbers/prism-line-numbers";
import "prismjs/plugins/line-numbers/prism-line-numbers.css";
import "prismjs/themes/prism-tomorrow.css";
import "prismjs/components/index";

type CodeBlockProps = {
  text: string;
  language: string;
};

export const CodeBlock = ({ text, language }: CodeBlockProps) => {
  const codeRef = useRef<HTMLElement>(null);

  useEffect(() => {
    if (codeRef.current) {
      Prism.highlightElement(codeRef.current);
    }
  }, []);

  return (
    <pre
      className={`line-numbers language-${language} h-full rounded-lg`}
      style={{ marginTop: 0 }}
    >
      <code
        ref={codeRef}
        className={`language-${language}`}
      >
        {text}
      </code>
    </pre>
  );
};
