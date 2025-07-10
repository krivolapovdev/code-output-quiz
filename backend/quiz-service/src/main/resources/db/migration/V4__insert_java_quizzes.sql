CREATE TYPE answer_choice_data AS
(
    choice answer_choice,
    text   TEXT
);

CREATE OR REPLACE FUNCTION insert_quiz_with_choices(
    quiz_code TEXT,
    lang_name TEXT,
    level_name TEXT,
    correct_answer answer_choice,
    explanation TEXT,
    answer_choices answer_choice_data[]
) RETURNS VOID AS
$$
DECLARE
    quiz_id     UUID;
    answer_item answer_choice_data;
BEGIN
    WITH q AS (
        INSERT INTO quizzes (
                             code,
                             correct_answer,
                             explanation,
                             programming_language_id,
                             difficulty_level_id
            )
            VALUES (quiz_code,
                    correct_answer,
                    explanation,
                    (SELECT id FROM programming_languages WHERE name = lang_name),
                    (SELECT id FROM difficulty_levels WHERE level = level_name))
            RETURNING id)
    SELECT id
    INTO quiz_id
    FROM q;

    FOREACH answer_item IN ARRAY answer_choices
        LOOP
            INSERT INTO quiz_answer_choices (quiz_id, choice, text)
            VALUES (quiz_id, answer_item.choice, answer_item.text);
        END LOOP;
END;
$$ LANGUAGE plpgsql;

SELECT insert_quiz_with_choices(
               $code$public class Main {
    public static void main(String[] args) {
        int x = 7;
        if (x % 2 == 0) {
            System.out.println("Even");
        } else {
            System.out.println("Odd");
        }
    }
}$code$,
               'JAVA',
               'BEGINNER',
               'B',
               'The variable `x` is assigned the value 7. The condition `x % 2 == 0` checks whether `x` is divisible by 2. Since 7 is not divisible by 2, the condition evaluates to `false`, and the `else` block executes, printing "Odd".',
               array [
                   row ('A', 'Even')::answer_choice_data,
                   row ('B', 'Odd')::answer_choice_data,
                   row ('C', '7')::answer_choice_data,
                   row ('D', 'Compilation Error')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$public class Main {
    public static void main(String[] args) {
        int a = 5;
        int b = a++;
        System.out.println(a);
        System.out.println(b);
    }
}$code$,
               'JAVA',
               'BEGINNER',
               'C',
               'The post-increment operator (`a++`) returns the original value of `a` (5) before incrementing it. Thus, `b` is assigned 5, while `a` becomes 6 after the increment operation. The first print statement outputs the updated `a` (6), and the second outputs the original value stored in `b` (5).',
               array [
                   row ('A', '5 5')::answer_choice_data,
                   row ('B', '5 6')::answer_choice_data,
                   row ('C', '6 5')::answer_choice_data,
                   row ('D', '6 6')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$public class Main {
    public static void main(String[] args) {
        int a = 5;
        int b = 2;
        double result = a / b;
        System.out.println(result);
    }
}$code$,
               'JAVA',
               'BEGINNER',
               'B',
               'In this code, both `a` and `b` are integers, so `a / b` performs integer division, resulting in `2`. This value is then stored as a `double`, becoming `2.0`. To get a precise decimal result like `2.5`, at least one operand must be a floating-point number (e.g., `a / 2.0`).',
               array [
                   row ('A', '2.5')::answer_choice_data,
                   row ('B', '2.0')::answer_choice_data,
                   row ('C', '2')::answer_choice_data,
                   row ('D', 'Compilation Error')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$public class Main {
    public static void main(String[] args) {
        int number = 9;
        for (int i = 1; i <= 3; i++) {
            number += i;
        }
        System.out.println(number);
    }
}$code$,
               'JAVA',
               'BEGINNER',
               'C',
               'The initial value of `number` is 9. The loop runs with i = 1, 2, 3, and each value is added to `number`. After all iterations, the value becomes 15 and is printed.',
               array [
                   row ('A', '12')::answer_choice_data,
                   row ('B', '13')::answer_choice_data,
                   row ('C', '15')::answer_choice_data,
                   row ('D', '18')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$public class Main {
    public static void main(String[] args) {
        String str1 = "Hello";
        String str2 = new String("Hello");
        System.out.println(str1 == str2);
    }
}$code$,
               'JAVA',
               'INTERMEDIATE',
               'B',
               'The `==` operator compares object references, not content. `str1` points to a string literal in the pool, while `str2` is a new String object on the heap. Even though their contents are equal, their references differ, so `str1 == str2` is false. Use `.equals()` for content comparison.',
               array [
                   row ('A', 'true')::answer_choice_data,
                   row ('B', 'false')::answer_choice_data,
                   row ('C', 'Compilation Error')::answer_choice_data,
                   row ('D', 'null')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$class Animal {
    void makeSound() {
        System.out.println("Animal sound");
    }
}

class Dog extends Animal {
    void makeSound() {
        System.out.println("Bark");
    }
}

public class Main {
    public static void main(String[] args) {
        Animal a = new Dog();
        a.makeSound();
    }
}$code$,
               'JAVA',
               'INTERMEDIATE',
               'B',
               'This is an example of **runtime polymorphism** in Java. The method `makeSound` is overridden in the `Dog` class, and since the actual object is `Dog`, its method is invoked, resulting in "Bark".',
               array [
                   row ('A', 'Animal sound')::answer_choice_data,
                   row ('B', 'Bark')::answer_choice_data,
                   row ('C', 'Compilation Error')::answer_choice_data,
                   row ('D', 'Runtime Error')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Integer> intList = new ArrayList<>();
        intList.add(1);
        intList.add(2);

        List list = intList;
        list.add("Hello");

        for(Object o : list) {
            System.out.print(o + " ");
        }
    }
}$code$,
               'JAVA',
               'INTERMEDIATE',
               'B',
               'Java generics use type erasure - type information is removed at runtime. When we assign `intList` to a raw `List`, we can add any type of object. The code compiles but allows adding a String to what was originally a List<Integer>. This demonstrates type erasure in Java generics and the risks of mixing generic and raw types.',
               array [
                   row ('A', '1 2')::answer_choice_data,
                   row ('B', '1 2 Hello')::answer_choice_data,
                   row ('C', 'Compilation Error')::answer_choice_data,
                   row ('D', 'Runtime Exception')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$class Vehicle {
    public void start() {
        System.out.println("Vehicle started");
    }
}

class Car extends Vehicle {
    @Override
    public void start() {
        System.out.println("Car started");
    }

    public void openTrunk() {
        System.out.println("Trunk opened");
    }
}

public class Main {
    public static void main(String[] args) {
        Vehicle v = new Car();
        v.start();
        v.openTrunk();
    }
}$code$,
               'JAVA',
               'INTERMEDIATE',
               'C',
               'While the reference `v` points to a `Car` object at runtime, its compile-time type is `Vehicle`. Since `openTrunk()` is not defined in `Vehicle`, the compiler throws an error. This illustrates that Java uses the **reference type** to determine accessible members at compile time, even though dynamic dispatch works for overridden methods like `start()`. To access `openTrunk()`, you would need to either cast the reference (`((Car)v).openTrunk()`), declare `v` as type `Car`, or define `openTrunk()` in the base class.',
               array [
                   row ('A', 'Car started\nTrunk opened')::answer_choice_data,
                   row ('B', 'Vehicle started\nTrunk opened')::answer_choice_data,
                   row ('C', 'Compilation error')::answer_choice_data,
                   row ('D', 'Runtime exception')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        int result = IntStream.rangeClosed(1, 5)
                              .parallel()
                              .reduce(0, (a, b) -> a - b);

        System.out.println(result);
    }
}$code$,
               'JAVA',
               'ADVANCED',
               'D',
               'The `reduce` operation is used on a parallel stream with a non-associative function (`a - b`). In parallel streams, the elements can be grouped and reduced in any order, so using a non-associative operator leads to **unpredictable and inconsistent results**. To ensure consistent behavior, always use associative operations in parallel reductions.',
               array [
                   row ('A', '-15')::answer_choice_data,
                   row ('B', '0')::answer_choice_data,
                   row ('C', '-5')::answer_choice_data,
                   row ('D', 'Unpredictable result')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("a", "b", "c");

        list.set(1, "x");
        list.add("y");

        System.out.println(list);
    }
}$code$,
               'JAVA',
               'ADVANCED',
               'D',
               'The `Arrays.asList()` method returns a fixed-size list backed by an array. Modifying elements with `set()` is allowed, but structural modifications like `add()` or `remove()` will throw `UnsupportedOperationException`. In this case, `list.add("y")` causes a runtime exception.',
               array [
                   row ('A', '[\n  a,\n  x,\n  c,\n  y\n]')::answer_choice_data,
                   row ('B', '[a, b, c, y]')::answer_choice_data,
                   row ('C', 'Compilation Error')::answer_choice_data,
                   row ('D', 'Runtime Exception')::answer_choice_data
                   ]
       );
