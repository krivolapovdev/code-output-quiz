SELECT insert_quiz_with_choices(
        $code$fn main() {
    let mut name = String::from("Rust");
    name.push('!');
    println!("{}", name);
}$code$,
        'RUST',
        'BEGINNER',
        'A',
        'The `String::from("Rust")` creates a mutable String. The `push` method appends a character to the end. So the final value is "Rust!" and it is printed.',
        'Rust!',
        'Rust',
        '!Rust',
        'Compilation Error'
     );

SELECT insert_quiz_with_choices(
        $code$fn main() {
    let numbers = [1, 2, 3, 4, 5];
    println!("{}", numbers[2]);
}$code$,
        'RUST',
        'BEGINNER',
        'C',
        'In Rust, arrays are zero-indexed. That means `numbers[2]` accesses the third element of the array, which is `3`.',
        '1',
        '2',
        '3',
        '4'
     );

SELECT insert_quiz_with_choices(
        $code$fn divide(a: f64, b: f64) -> Option<f64> {
    if b == 0.0 {
        None
    } else {
        Some(a / b)
    }
}

fn main() {
    match divide(10.0, 2.0) {
        Some(result) => println!("Result: {}", result),
        None => println!("Cannot divide by zero"),
    }
}$code$,
        'RUST',
        'INTERMEDIATE',
        'C',
        'The `divide` function safely divides two f64 values, returning Some(result) if the divisor is not zero. Since 2.0 is not zero, it returns Some(5.0), and the match arm prints "Result: 5.0".',
        'None',
        'Result: 5',
        'Result: 5.0',
        'Cannot divide by zero'
     );

SELECT insert_quiz_with_choices(
        $code$use std::rc::Rc;

struct Node {
    value: i32,
    next: Option<Rc<Node>>,
}

fn main() {
    let node1 = Rc::new(Node { value: 1, next: None });
    let node2 = Rc::new(Node { value: 2, next: Some(Rc::clone(&node1)) });

    println!("node2 -> node1: {}", node2.next.as_ref().unwrap().value);
    println!("Strong count of node1: {}", Rc::strong_count(&node1));
}$code$,
        'RUST',
        'ADVANCED',
        'C',
        'This code uses Rc to share ownership of node1. Rc::clone increments the strong count to 2. Accessing value of node1 from node2.next works, and the output shows "node2 -> node1: 1" and "Strong count of node1: 2".',
        'node2 -> node1: 2 and Strong count of node1: 1',
        'node2 -> node1: 1 and Strong count of node1: 1',
        'node2 -> node1: 1 and Strong count of node1: 2',
        'Compilation Error'
     );
