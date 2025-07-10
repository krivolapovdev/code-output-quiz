SELECT insert_quiz_with_choices(
               $code$#include <stdio.h>

int main() {
    int numbers[] = {1, 2, 3, 4, 5};
    int result[5];
    int i, j = 0;

    for (i = 0; i < 5; i++) {
        if (numbers[i] % 2 == 0) {
            result[j] = numbers[i] * numbers[i];
            j++;
        }
    }

    for (i = 0; i < j; i++) {
        printf("%d ", result[i]);
    }

    return 0;
}$code$,
               'C',
               'BEGINNER',
               'B',
               'This program squares only even numbers from the array `numbers[]`. The numbers 2 and 4 are even, and their squares (4 and 16) are returned.',
               array [
                   row ('A', '1 4 9 16 25')::answer_choice_data,
                   row ('B', '4 16')::answer_choice_data,
                   row ('C', '2 4')::answer_choice_data,
                   row ('D', '1 9 25')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$#include <stdio.h>

void swap(int *x, int *y) {
    int temp = *x;
    *x = *y;
    *y = temp;
}

int main() {
    int a = 5, b = 10;

    swap(&a, &b);
    printf("a = %d, b = %d\\n", a, b);

    return 0;
}$code$,
               'C',
               'BEGINNER',
               'B',
               'The `swap` function swaps the values of `a` and `b` using pointers. After the swap, `a` becomes 10 and `b` becomes 5.',
               array [
                   row ('A', 'a = 5, b = 10')::answer_choice_data,
                   row ('B', 'a = 10, b = 5')::answer_choice_data,
                   row ('C', 'Compilation error')::answer_choice_data,
                   row ('D', 'Undefined behavior')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$#include <stdio.h>

void reverse_array(int arr[], int size) {
    int start = 0, end = size - 1;
    while (start < end) {
        int temp = arr[start];
        arr[start] = arr[end];
        arr[end] = temp;
        start++;
        end--;
    }
}

int main() {
    int arr[] = {1, 2, 3, 4, 5};
    int size = sizeof(arr) / sizeof(arr[0]);

    reverse_array(arr, size);

    for (int i = 0; i < size; i++) {
        printf("%d ", arr[i]);
    }

    return 0;
}$code$,
               'C',
               'INTERMEDIATE',
               'B',
               'The function `reverse_array` reverses the elements of the array. The initial array `{1, 2, 3, 4, 5}` is reversed to `{5, 4, 3, 2, 1}`.',
               array [
                   row ('A', '1 2 3 4 5')::answer_choice_data,
                   row ('B', '5 4 3 2 1')::answer_choice_data,
                   row ('C', 'Compilation error')::answer_choice_data,
                   row ('D', 'Undefined behavior')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$#include <stdio.h>
#include <stdlib.h>

void *memory_allocate(size_t size) {
    void *ptr = malloc(size);
    if (!ptr) {
        printf("Memory allocation failed\\n");
        exit(1);
    }
    return ptr;
}

int main() {
    int *arr;
    size_t n = 5;
    arr = (int *)memory_allocate(n * sizeof(int));

    for (size_t i = 0; i < n; i++) {
        arr[i] = i + 1;
    }

    for (size_t i = 0; i < n; i++) {
        printf("%d ", arr[i]);
    }

    free(arr);
    return 0;
}$code$,
               'C',
               'ADVANCED',
               'B',
               'The program correctly allocates memory for an integer array and fills it with values 1 through 5. After printing the array, the memory is freed.',
               array [
                   row ('A', 'Memory allocation failed')::answer_choice_data,
                   row ('B', '1 2 3 4 5')::answer_choice_data,
                   row ('C', '0 1 2 3 4')::answer_choice_data,
                   row ('D', 'Compilation error due to invalid pointer type')::answer_choice_data
                   ]
       );

SELECT insert_quiz_with_choices(
               $code$#include <stdio.h>
#include <stdlib.h>

int *merge_arrays(int *arr1, int size1, int *arr2, int size2) {
    int *result = (int *)malloc((size1 + size2) * sizeof(int));
    if (!result) {
        printf("Memory allocation failed\\n");
        exit(1);
    }

    for (int i = 0; i < size1; i++) {
        result[i] = arr1[i];
    }

    for (int i = 0; i < size2; i++) {
        result[size1 + i] = arr2[i];
    }

    return result;
}

int main() {
    int arr1[] = {1, 2, 3};
    int arr2[] = {4, 5, 6};
    int size1 = sizeof(arr1) / sizeof(arr1[0]);
    int size2 = sizeof(arr2) / sizeof(arr2[0]);

    int *merged = merge_arrays(arr1, size1, arr2, size2);

    for (int i = 0; i < size1 + size2; i++) {
        printf("%d ", merged[i]);
    }

    free(merged);
    return 0;
}$code$,
               'C',
               'ADVANCED',
               'B',
               'The `merge_arrays` function merges two arrays into a new dynamically allocated array. The merged result is "1 2 3 4 5 6".',
               array [
                   row ('A', 'Memory allocation failed')::answer_choice_data,
                   row ('B', '1 2 3 4 5 6')::answer_choice_data,
                   row ('C', '1 2 3 3 4 5 6')::answer_choice_data,
                   row ('D', 'Compilation error due to invalid pointer type')::answer_choice_data
                   ]
       );
