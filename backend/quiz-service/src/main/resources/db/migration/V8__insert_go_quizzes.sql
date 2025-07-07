SELECT insert_quiz_with_choices(
        $code$package main

import "fmt"

func main() {
    numbers := []int{1, 2, 3, 4, 5}
    sum := 0
    for _, num := range numbers {
        sum += num
    }
    fmt.Println(sum)
}$code$,
        'GO',
        'BEGINNER',
        'B',
        'The program defines a slice `numbers` containing the integers 1 through 5. The `for` loop iterates over the elements of the slice, and the `sum` variable accumulates the total of all the numbers in the slice. The sum of 1 + 2 + 3 + 4 + 5 is 15, which is printed.',
        '10',
        '15',
        '5',
        '0'
     );

SELECT insert_quiz_with_choices(
        $code$package main

import "fmt"

func main() {
    nums := []int{1, 2, 3, 4, 5}
    result := mapValues(nums, func(x int) int {
        return x * x
    })
    fmt.Println(result)
}

func mapValues(nums []int, f func(int) int) []int {
    var result []int
    for _, num := range nums {
        result = append(result, f(num))
    }
    return result
}$code$,
        'GO',
        'INTERMEDIATE',
        'A',
        'The program defines a slice `nums` containing integers 1 through 5. The `mapValues` function takes the slice and a function `f`, which squares each element of the slice. The resulting squared values (1, 4, 9, 16, 25) are stored in the `result` slice and printed.',
        '[1, 4, 9, 16, 25]',
        '[1, 2, 3, 4, 5]',
        '[0, 1, 4, 9, 16]',
        '[1, 2, 3, 4, 5, 6]'
     );

SELECT insert_quiz_with_choices(
        $code$package main

import "fmt"

type Person struct {
    Name string
    Age  int
}

func main() {
    people := []Person{
        {"Alice", 30},
        {"Bob", 25},
        {"Charlie", 35},
    }

    result := filter(people, func(p Person) bool {
        return p.Age > 30
    })

    fmt.Println(result)
}

func filter(people []Person, f func(Person) bool) []Person {
    var result []Person
    for _, p := range people {
        if f(p) {
            result = append(result, p)
        }
    }
    return result
}$code$,
        'GO',
        'ADVANCED',
        'B',
        'The program defines a slice `people` containing `Person` structs. The `filter` function filters the slice by a condition: it returns people whose age is greater than 30. Thus, only "Charlie" with age 35 is included in the result.',
        '[{"Alice", 30}, {"Bob", 25}]',
        '[{"Charlie", 35}]',
        '[{"Alice", 30}, {"Charlie", 35}]',
        '[{"Bob", 25}]'
     );
