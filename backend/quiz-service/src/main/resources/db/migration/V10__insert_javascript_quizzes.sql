SELECT insert_quiz_with_choices(
               $code$let x = 10;
let y = "10";

console.log(x == y);$code$,
               'JAVASCRIPT',
               'BEGINNER',
               'B',
               'In JavaScript, the `==` operator performs type coercion. Since x is a number and y is a string, y is converted to a number. So the comparison becomes 10 == 10, which is true.',
               array [
                   row ('A', 'false')::answer_choice_data,
                   row ('B', 'true')::answer_choice_data,
                   row ('C', 'TypeError')::answer_choice_data,
                   row ('D', 'undefined')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$function greet(name = "Guest") {
    return "Hello, " + name + "!";
}

console.log(greet());
console.log(greet("Alice"));$code$,
               'JAVASCRIPT',
               'INTERMEDIATE',
               'B',
               'The function has a default parameter "Guest". When no argument is passed, "Guest" is used. When "Alice" is passed, it overrides the default.',
               array [
                   row ('A', 'Hello, undefined! and Hello, Alice!')::answer_choice_data,
                   row ('B', 'Hello, Guest! and Hello, Alice!')::answer_choice_data,
                   row ('C', 'Hello! and Hello, Alice!')::answer_choice_data,
                   row ('D', 'undefined and Hello, Alice!')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$const obj = {
  value: 10,
  getValue: function () {
    return () => this.value;
  },
};

const getVal = obj.getValue();
console.log(getVal());$code$,
               'JAVASCRIPT',
               'ADVANCED',
               'B',
               'Arrow functions do not have their own `this` and instead inherit it from the surrounding context. Here, `this` refers to `obj`, so `this.value` is 10.',
               array [
                   row ('A', 'undefined')::answer_choice_data,
                   row ('B', '10')::answer_choice_data,
                   row ('C', 'null')::answer_choice_data,
                   row ('D', 'ReferenceError')::answer_choice_data
                   ]
       );
