SELECT insert_quiz_with_choices(
               $code$using System;

class Program
{
    static void Main()
    {
        int[] numbers = { 1, 2, 3, 4, 5 };
        int sum = 0;
        foreach (var number in numbers)
        {
            if (number % 2 == 0)
            {
                sum += number;
            }
        }
        Console.WriteLine(sum);
    }
}$code$,
               'CSHARP',
               'BEGINNER',
               'B',
               'The code sums the even numbers from the array, which are 2 and 4, resulting in 6.',
               array [
                   row ('A', '15')::answer_choice_data,
                   row ('B', '6')::answer_choice_data,
                   row ('C', '9')::answer_choice_data,
                   row ('D', '10')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$using System;
using System.Linq;

class Program
{
    static void Main()
    {
        int[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        var evenNumbers = numbers.Where(n => n % 2 == 0).ToArray();
        Console.WriteLine(string.Join(", ", evenNumbers));
    }
}$code$,
               'CSHARP',
               'INTERMEDIATE',
               'B',
               'The LINQ query filters even numbers from the array, which are 2, 4, 6, 8, and 10.',
               array [
                   row ('A', '1, 2, 3, 4, 5, 6, 7, 8, 9, 10')::answer_choice_data,
                   row ('B', '2, 4, 6, 8, 10')::answer_choice_data,
                   row ('C', '1, 3, 5, 7, 9')::answer_choice_data,
                   row ('D', '2, 4, 6, 8')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$using System;
using System.Collections.Generic;
using System.Linq;

class Program
{
    static void Main()
    {
        var numbers = new List<int> { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        var oddSquares = numbers.Where(n => n % 2 != 0)
                                 .Select(n => n * n)
                                 .OrderByDescending(n => n)
                                 .ToList();
        Console.WriteLine(string.Join(", ", oddSquares));
    }
}$code$,
               'CSHARP',
               'ADVANCED',
               'A',
               'The code filters the odd numbers, squares them, and orders them in descending order. The result is 81, 49, 25, 9, 1.',
               array [
                   row ('A', '1, 9, 25, 49, 81')::answer_choice_data,
                   row ('B', '1, 9, 25, 49, 81, 121')::answer_choice_data,
                   row ('C', '1, 49, 25, 9, 81')::answer_choice_data,
                   row ('D', '1, 49, 9, 25, 81')::answer_choice_data
                   ]
       );
