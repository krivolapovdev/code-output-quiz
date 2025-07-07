SELECT insert_quiz_with_choices(
        $code$#include <iostream>
#include <vector>

std::vector<int> square_evens(std::vector<int> numbers) {
    std::vector<int> result;
    for (int num : numbers) {
        if (num % 2 == 0) {
            result.push_back(num * num);
        }
    }
    return result;
}

int main() {
    std::vector<int> numbers = {1, 2, 3, 4, 5};
    std::vector<int> result = square_evens(numbers);

    for (int num : result) {
        std::cout << num << " ";
    }
    return 0;
}$code$,
        'CPP',
        'BEGINNER',
        'B',
        'The function squares only even numbers from the input list. The even numbers are 2 and 4, and their squares are 4 and 16.',
        '1 4 9 16 25',
        '4 16',
        '2 4',
        '1 9 25'
     );

SELECT insert_quiz_with_choices(
        $code$#include <iostream>
#include <vector>
#include <algorithm>

void remove_duplicates(std::vector<int>& numbers) {
    std::sort(numbers.begin(), numbers.end());
    auto last = std::unique(numbers.begin(), numbers.end());
    numbers.erase(last, numbers.end());
}

int main() {
    std::vector<int> numbers = {1, 3, 2, 2, 4, 1, 5, 3};
    remove_duplicates(numbers);

    for (int num : numbers) {
        std::cout << num << " ";
    }
    return 0;
}$code$,
        'CPP',
        'INTERMEDIATE',
        'A',
        'The function removes duplicates by first sorting the vector, then using unique to eliminate adjacent duplicates, and finally erasing extra elements. The final output is 1 2 3 4 5.',
        '1 2 3 4 5',
        '1 1 2 2 3 3 4 5',
        '1 2 3 2 4 1 5',
        '3 2 1 4 5'
     );

SELECT insert_quiz_with_choices(
        $code$#include <iostream>
#include <vector>
#include <algorithm>

void merge_sorted_arrays(std::vector<int>& arr1, std::vector<int>& arr2) {
    std::vector<int> merged;
    int i = 0, j = 0;

    while (i < arr1.size() && j < arr2.size()) {
        if (arr1[i] < arr2[j]) {
            merged.push_back(arr1[i++]);
        } else {
            merged.push_back(arr2[j++]);
        }
    }

    while (i < arr1.size()) {
        merged.push_back(arr1[i++]);
    }

    while (j < arr2.size()) {
        merged.push_back(arr2[j++]);
    }

    arr1 = merged;
}

int main() {
    std::vector<int> arr1 = {1, 3, 5, 7};
    std::vector<int> arr2 = {2, 4, 6, 8};
    merge_sorted_arrays(arr1, arr2);

    for (int num : arr1) {
        std::cout << num << " ";
    }
    return 0;
}$code$,
        'CPP',
        'ADVANCED',
        'A',
        'The function merges two sorted arrays into a single sorted array. The output is 1 2 3 4 5 6 7 8.',
        '1 2 3 4 5 6 7 8',
        '1 3 5 7 2 4 6 8',
        '2 4 6 8 1 3 5 7',
        '1 2 3 4 5 6 7'
     );
