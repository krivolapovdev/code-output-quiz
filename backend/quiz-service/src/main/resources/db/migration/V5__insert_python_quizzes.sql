SELECT insert_quiz_with_choices(
               $code$x = "10"
y = 5

result = x * y
print(result)$code$,
               'PYTHON',
               'BEGINNER',
               'B',
               'In Python, multiplying a string by an integer repeats the string. So `"10" * 5` produces `"1010101010"`. It’s a valid operation, and no error is thrown.',
               '50',
               '1010101010',
               'TypeError',
               '105'
       );

SELECT insert_quiz_with_choices(
               $code$def check_number(n):
    if n % 2 == 0:
        return "Even"
    else:
        return "Odd"

print(check_number(9))$code$,
               'PYTHON',
               'BEGINNER',
               'B',
               '9 is not divisible by 2 evenly (`9 % 2 == 1`), so the function returns "Odd".',
               'Even',
               'Odd',
               '9',
               'Error'
       );

SELECT insert_quiz_with_choices(
               $code$def modify_data(a, b):
    a[0] = 100
    b[1] = 200

lst = [1, 2, 3]
tpl = (4, 5, 6)

modify_data(lst, tpl)
print(lst, tpl)$code$,
               'PYTHON',
               'INTERMEDIATE',
               'D',
               'Tuples are immutable in Python. The code attempts to modify a tuple element at b[1] = 200, which raises a TypeError.',
               '[100, 2, 3] (4, 5, 6)',
               '[1, 2, 3] (4, 5, 6)',
               '[100, 2, 3] (4, 200, 6)',
               'TypeError'
       );

SELECT insert_quiz_with_choices(
               $code$def square_evens(numbers):
    return [x**2 for x in numbers if x % 2 == 0]

result = square_evens([1, 2, 3, 4, 5])
print(result)$code$,
               'PYTHON',
               'INTERMEDIATE',
               'B',
               'The function uses list comprehension to square only even numbers. In the input list, 2 and 4 are even, so their squares (4 and 16) are returned.',
               '[1, 4, 9, 16, 25]',
               '[4, 16]',
               '[2, 4]',
               '[1, 9, 25]'
       );

SELECT insert_quiz_with_choices(
               $code$def merge_dicts(dict1, dict2):
    for key, value in dict2.items():
        if key in dict1:
            dict1[key] += value
        else:
            dict1[key] = value
    return dict1

dict1 = {'a': 1, 'b': 2}
dict2 = {'b': 3, 'c': 4}
result = merge_dicts(dict1, dict2)
print(result)$code$,
               'PYTHON',
               'ADVANCED',
               'A',
               'The function merges two dictionaries by adding the values of common keys. For key "b", the values 2 and 3 are added to give 5. Keys "a" and "c" are added as they appear only in one dictionary each.',
               '{''a'': 1, ''b'': 5, ''c'': 4}',
               '{''a'': 1, ''b'': 2, ''c'': 4}',
               '{''a'': 2, ''b'': 5, ''c'': 4}',
               '{''a'': 2, ''b'': 2, ''c'': 4}'
       );

SELECT insert_quiz_with_choices(
               $code$def cube_roots(numbers):
    return [round(x ** (1/3), 2) for x in numbers if round(x ** (1/3)) ** 3 == x]

result = cube_roots([1, 8, 27, 64, 100, 125])
print(result)$code$,
               'PYTHON',
               'ADVANCED',
               'A',
               'The function calculates the cube root of each number in the list that is a perfect cube. Numbers 1, 8, 27, 64, and 125 are perfect cubes, so their cube roots (1.0, 2.0, 3.0, 4.0, 5.0) are returned.',
               '{1.0, 2.0, 3.0, 4.0, 5.0}',
               '{1.0, 2.0, 3.0, 4.0, 6.0}',
               '{2.0, 3.0, 4.0, 5.0}',
               '{1.0, 2.0, 3.0, 4.0}'
       );
