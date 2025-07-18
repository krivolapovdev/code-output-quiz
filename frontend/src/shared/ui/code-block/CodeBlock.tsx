import Prism from "prismjs";
import { useEffect, useRef } from "react";
import "prismjs/plugins/line-numbers/prism-line-numbers";
import "prismjs/plugins/line-numbers/prism-line-numbers.css";
import "prismjs/themes/prism-tomorrow.css";

import "prismjs/components/prism-java";
import "prismjs/components/prism-python";
import "prismjs/components/prism-c";
import "prismjs/components/prism-cpp";
import "prismjs/components/prism-go";
import "prismjs/components/prism-csharp";
import "prismjs/components/prism-javascript";
import "prismjs/components/prism-rust";
import "prismjs/components/prism-sql";

type CodeBlockProps = {
  code: string;
  language: string;
};

export const CodeBlock = ({ code, language }: CodeBlockProps) => {
  const codeRef = useRef<HTMLElement>(null);

  useEffect(() => {
    if (codeRef.current) {
      Prism.highlightElement(codeRef.current);
    }
  }, []);

  return (
    <pre
      tabIndex={-1}
      className={`line-numbers language-${language} h-full rounded-lg`}
      style={{ marginTop: 0 }}
    >
      <code
        ref={codeRef}
        className={`language-${language}`}
        tabIndex={-1}
      >
        {code}
      </code>
    </pre>
  );
};
