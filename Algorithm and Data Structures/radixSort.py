import copy

"""
---------------------------------------------Question 1--------------------------------------------
"""


def find_max(list_input: list) -> int:
    """
    Function to find the largest number in list_input

    :param list_input: list of integers to find the largest number from
    :return: largest number in list_input
    :complexity: O(N) where N is the number of elements in list_input
    """
    output = list_input[0]
    for i in list_input:
        if i > output:
            output = i
    return output


def find_digit_count(int_input: int) -> int:
    """
    Function to find the number of digits in int_input

    :param int_input: integer to get number of digits from
    :return: number of digits in int_input
    :complexity: O(k) where k is the number of digits in int_input
    """
    counter = 0
    if int_input > 0:
        while int_input > 0:
            int_input = int_input // 10
            counter += 1
        return counter
    else:
        return 1


def get_current_digit(int_input: int, power: int) -> int:
    """
    Function to return the exact numeric value at the position of power

    :param int_input: integer to get the pointed digit from
    :param power: position to get the integer from
    :return: an integer at position power from int_input
    :complexity: O(1)
    """
    base = 10
    return int_input // (base ** power) % base


def modified_counting_sort(list_input: list, power: int) -> list:
    """
    Function to sort list_input according to a specified value at position power
    (power here means the name of the variable)

    :param list_input: list of integers to be sorted
    :param power: position of the value the sort should base on
    :return: sorted list_input based on their value at position power
    :complexity: O(N) where N is the number of elements in list_input
    """
    output = []
    count_list = []
    for _ in range(0, 10):
        count_list.append([])
    for item in list_input:
        temp = get_current_digit(item, power)
        count_list[temp].append(item)
    for index in count_list:
        for item in index:
            output.append(item)
    return output


def radix_sort(list_input: list) -> list:
    """
    Function to sort list_input through the usage of radix sort

    :param list_input: list of integers to be sorted
    :return: sorted list_input in ascending order
    :complexity: O(N*k) where N is the number of elements in list_input
                              k is the number of digits in list_input
    """
    max_input = find_max(list_input)
    max_digit_count = find_digit_count(max_input)
    for power in range(0, max_digit_count):
        list_input = (modified_counting_sort(list_input, power))
    return list_input


def best_interval(list_input: list, t: int) -> tuple:
    """
    Function to find the best interval such that there will be most number elements in list_input in the timeframe of t

    :param list_input: list of transactions to find best interval from
    :param t: timeframe such that there will be most elements in list_input in the timeframe
    :return: a two element tuple, (best_t, count). best_t is the time such
             that the interval starting at best_t and ending at best_t + t contains more elements from
             transactions than any other interval of length t.
    :complexity: O(N*k) where N is the number of elements in list_input
                              k is the number of digits in list_input
    """
    best_counter = 1
    best_index = 0
    list_input = radix_sort(list_input)
    for index in range(len(list_input) - 1, -1, -1):
        check_loop = True
        counter = 1
        first_item = list_input[index]
        next_index = index - 1
        if next_index > -1:
            next_item = list_input[next_index]
            while check_loop:
                if next_item >= first_item - (t - 1):
                    counter += 1
                    next_index -= 1
                    if next_index > -1:
                        next_item = list_input[next_index]
                    else:
                        check_loop = False
                else:
                    check_loop = False
            if counter >= best_counter:
                best_counter = counter
                best_index = index
    output_interval = list_input[best_index]
    for _ in range(0, t):
        if output_interval > 0:
            output_interval -= 1
        else:
            break
    return output_interval, best_counter


"""
---------------------------------------------Question 2--------------------------------------------
"""


def find_max_len(list_input: list) -> int:
    """
    Function to find the length of longest string in list_input

    :param list_input: list of strings to find the length of longest string from
    :return: length of longest string
    :complexity: O(N) where N is the number of elements in list_input
    """
    output = 0
    for i in list_input:
        if len(i) > output:
            output = len(i)
    return output


def sort_by_len(list_input: list) -> list:
    """
    Function to sort the elements in the list_input by their length

    :param list_input: list of strings to be sorted
    :return: sorted list of strings by length
    :complexity: O(N+M) where N is the number of elements in list_input,
                              M is the length of longest string in list_input
    """
    output = []
    count_list = []
    max_input = find_max_len(list_input)
    for _ in range(0, max_input + 1):
        count_list.append([])
    for index in list_input:
        count_list[len(index)].append(index)
    for index in count_list:
        for item in index:
            output.append(item)
    return output


def rearrange_string(input_string: str) -> str:
    """
    Function to rearrange the characters in a string in alphabetical order

    :param input_string: string to be sorted
    :return: sorted string
    "complexity: O(M) where M is the number of char in input_string
    """
    count_list = []
    output = ""
    for _ in range(0, 26):
        count_list.append([])
    for current_char in input_string:
        count_list[ord(current_char) - 97].append(current_char)
    for index in count_list:
        for item in index:
            output = output + item
    return output


def string_radix_sort(list_input: list, char_count: int) -> list:
    """
    Function to sort a list of strings with the same length by alphabetical order

    :param list_input: list of strings with the same length to be sorted
                       (each individual string should already be sorted by alphabetical order)
    :param char_count: length of each strings in the list
    :return: sorted list of strings in alphabetical order
    :complexity: O(N*M) where N is the number of elements in list_input,
                              M is the number of characters in the longest string
    """
    # O(no.elements * char_count)
    for char_position in range(char_count - 1, -1, -1):
        list_input = (string_radix_sort_aux(list_input, char_position))
    return list_input


def string_radix_sort_aux(list_input: list, char_position: int) -> list:
    """
    Function that implements counting sort to sort list of strings based on the character at char_position

    :param list_input: list of strings with the same length to be sorted
    :param char_position: position of the character in the string to base the sort on
    :return: sorted list of strings based on the character at char_position
    :complexity: O(N) where N is the number of elements in list_input
    """
    # O(no.elements)
    output = []
    count_list = []
    for _ in range(0, 26):
        count_list.append([])
    for item in list_input:
        count_list[ord(item[0][char_position]) - 97].append(item)
    for index in count_list:
        for item in index:
            output.append(item)
    return output


def list_preprocessing(list_input: list, list_number: int) -> list:
    """
    Function to preprocess the list of strings into more suitable form by,
    1) Sort the elements in the list by their length,
    2) Add in additional information for each elements
       (which list they are from ? 1 or 2 ?),(original word before rearranged)
    3) rearrange each string in alphabetical order
    4) sort the list again by alphabetical order

    :param list_input: list of strings to be processed
    :param list_number: number identifier of the input list
    :return: properly pre-processed list of arrays e.g.
            ["dad"] -> [["add",1,"dad"]]
    :complexity: O(N*M) where N is the number of elements in list_input,
                              M is the number of characters in the longest string
    """
    # Step1
    list_input = sort_by_len(list_input)
    output = []

    # Create output array to store values
    for _ in range(0, len(list_input)):
        output.append([])

    # Step2
    for index in range(0, len(list_input)):
        output[index] = [list_input[index], list_number, copy.deepcopy(list_input[index])]

    # Step3
    for index in range(0, len(list_input)):
        output[index] = [rearrange_string(output[index][0]), list_number, copy.deepcopy(list_input[index])]

    # Step4
    run = True
    char_count = 1
    start_pointer = 0
    end_pointer = 0
    while run:
        if char_count <= find_max_len(list_input):
            if (end_pointer < len(output)) and len(output[end_pointer][0]) == char_count:
                end_pointer += 1
            else:
                output[start_pointer:end_pointer] = string_radix_sort(output[start_pointer:end_pointer], char_count)
                start_pointer = end_pointer
                char_count += 1
        else:
            run = False

    return output


def merge(list1: list, list2: list) -> list:
    """
    Function to merge two sorted lists of string with the same length into one for easier comparison.

    :param list1: first sorted list to be merged
    :param list2: second sorted list to be merged
    :return: sorted merged list of list1 and list2
    :complexity: O(N1+N2) where N1 is the number of elements in list1,
                                N2 is the number of elements in list2
    """
    output = []
    pointer1 = 0
    pointer2 = 0

    """
    Standard comparison, with the exception where if both list1 and list2 has the same elements, 
    the program priorities pushing elements in list2
    """
    while (pointer1 < len(list1)) and (pointer2 < len(list2)):
        if list2[pointer2][0] == list1[pointer1][0]:
            output.append(list2[pointer2])
            output.append(list1[pointer1])
            pointer2 += 1
            pointer1 += 1
        elif list2[pointer2][0] < list1[pointer1][0]:
            output.append(list2[pointer2])
            pointer2 += 1
        else:
            output.append(list1[pointer1])
            pointer1 += 1

    if pointer1 < len(list1):
        for i in range(pointer1, len(list1)):
            output.append(list1[i])
    if pointer2 < len(list1):
        for i in range(pointer2, len(list2)):
            output.append(list2[i])

    return output


def words_with_anagrams(list1: list, list2: list) -> list:
    """
    Function to find all words in the first list which have an anagram in the second list.

    :param list1: first list of strings to find anagram from
    :param list2: second list of strings to find anagram from
    :return: A list of strings from list1 which have at least one anagram appearing in list2
    :complexity: O(L1M1 + L2M2)  where L1 is the number of elements in list1
                                       L2 is the number of elements in list2
                                       M1 is the number of characters in the longest string in list1
                                       M2 is the number of characters in the longest string in list2
    """
    run = True
    pointer1_start = 0
    pointer1_end = 0
    pointer2_start = 0
    pointer2_end = 0
    output = []

    # pre-process both lists of inputs
    list1 = list_preprocessing(list1, 1)
    list2 = list_preprocessing(list2, 2)

    if len(list2) == 0:
        return []

    while run:

        # In each iteration, first find all the strings with the same length
        current_length = len(list2[pointer2_start][0])
        if pointer1_end < len(list1) and len(list1[pointer1_end][0]) < current_length:
            pointer1_start += 1
            pointer1_end += 1
        elif pointer2_end < len(list2) and len(list2[pointer2_end][0]) == current_length:
            pointer2_end += 1
            if pointer1_end < len(list1) and len(list1[pointer1_end][0]) == current_length:
                pointer1_end += 1
        elif pointer1_end < len(list1) and len(list1[pointer1_end][0]) == current_length:
            pointer1_end += 1

        # When no more string of the same length are found, merge them and begin comparison
        else:
            combined_list = merge(list1[pointer1_start:pointer1_end], list2[pointer2_start:pointer2_end])
            combined_list_pointer = 0
            current_word = ""
            while combined_list_pointer < len(combined_list):
                if combined_list[combined_list_pointer][1] == 2:
                    current_word = combined_list[combined_list_pointer][0]
                elif combined_list[combined_list_pointer][1] == 1:
                    if combined_list[combined_list_pointer][0] == current_word:
                        output.append(combined_list[combined_list_pointer][2])
                combined_list_pointer += 1

            pointer1_start = pointer1_end
            pointer2_start = pointer2_end

            # When there are no more elements in either list, end the loop
            if pointer1_start >= len(list1) or pointer2_start >= len(list2):
                run = False

    return output
