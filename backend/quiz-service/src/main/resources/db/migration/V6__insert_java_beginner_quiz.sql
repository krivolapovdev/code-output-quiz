SELECT insert_quiz_with_choices(
               $code$int x = 5;
int y = 2;
System.out.println(x / y);$code$,
               'JAVA',
               'BEGINNER',
               'B',
               'In Java, integer division truncates the decimal part. 5 / 2 equals 2, not 2.5.',
               '[
                 {
                   "choice": "A",
                   "text": "2.5"
                 },
                 {
                   "choice": "B",
                   "text": "2"
                 },
                 {
                   "choice": "C",
                   "text": "3"
                 },
                 {
                   "choice": "D",
                   "text": "Compilation error"
                 }
               ]'::jsonb
       );
