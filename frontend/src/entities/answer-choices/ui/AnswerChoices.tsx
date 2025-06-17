type AnswerChoicesProps = {
  options: string[];
  onSelect: (option: string) => void;
};

const labelColors = ['text-green-600', 'text-blue-600', 'text-yellow-600', 'text-purple-600'];

export const AnswerChoices = ({ options, onSelect }: AnswerChoicesProps) => {
  return (
    <div className='mt-6 grid grid-cols-2 gap-4'>
      {options.map((option, index) => (
        <button
          key={option}
          type='button'
          onClick={() => onSelect(option)}
          className='flex cursor-pointer items-start gap-2 rounded-md border border-transparent bg-white px-4 py-2 text-left text-black transition-all duration-200 hover:border-blue-500'
        >
          <span className={`font-semibold ${labelColors[index % labelColors.length]}`}>
            {String.fromCharCode('A'.charCodeAt(0) + index)})
          </span>{' '}
          <span>{option}</span>
        </button>
      ))}
    </div>
  );
};
