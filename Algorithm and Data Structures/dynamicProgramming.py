def best_schedule(weekly_income: list, competitions: list) -> int:
    """
    Function to return an integer, which is the maximum amount of money that can be earned by an athlete
    :param weekly_income: list a stores the amount of money the athlete will
                          earn working as a personal trainer in a specific week
    :param competitions: list of tuples that contain start time, end time and winnings of a competition
    :return: integer that represents the maximum amount of money that can be earned by an athlete
    :time complexity: O(nlogn) where n is the total number of elements in weekly_income and competitions put together.
    :space complexity: O(n) where n is the total number of elements in weekly_income and competitions put together.
    """
    weekly_income = weekly_income_preprocess(weekly_income)
    option_list = merge_sort(weekly_income + competitions)
    memo = [0] * (len(weekly_income) + 1)
    for index in range(0, len(option_list)):
        start = option_list[index][0]
        end = option_list[index][1]
        profit = option_list[index][2]
        memo[end + 1] = max(memo[end + 1], memo[start] + profit)
    return memo[-1]


def weekly_income_preprocess(input_list: list) -> list:
    """
    Function that preprocess input_list into a more appropriate form
    :param input_list: list to be preprocessed
    :return: preprocessed input_list
    :time complexity: O(n) where: n is the number of elements in input_list
    :space complexity: O(n) where: n is the number of elements in input_list
    """
    output = []
    for i in range(0, len(input_list)):
        output.append((i, i, input_list[i]))
    return output


def merge_sort(input_list: list) -> list:
    """
    Sort function that sorts input_list using recursion
    :param input_list: list to be sorted
    :return: sorted input_list
    :time complexity: O(nlogn) where: n is the number of elements in input_list
    :space complexity: O(n) where: n is the number of elements in input_list
    """
    n = len(input_list)
    if n <= 1:
        return input_list
    else:
        left = merge_sort(input_list[:n // 2])
        right = merge_sort(input_list[n // 2:])
    return merge(left, right)


def merge(list1: list, list2: list) -> list:
    """
    Function that merges two list in ascending order
    :param list1: first list to be merged
    :param list2: second list to be merged
    :return: merged list in ascending order
    :time complexity: O(n) where: n is the number of elements in list1 + list2
    :space complexity: O(n) where: n is the number of elements in list1 + list2
    """
    output = []
    i, j = 0, 0
    while i < len(list1) and j < len(list2):
        if list1[i][1] <= list2[j][1]:
            output += [list1[i]]
            i += 1
        else:
            output += [list2[j]]
            j += 1
    return output + list1[i:] + list2[j:]


def best_itinerary(profit: list, quarantine_time: list, home: int) -> int:
    """
    Function that returns an integer, which is the maximum amount of money that can be earned by a salesperson
    :param profit: A list of lists that stores the earnable profit at certain city on certain day
    :param quarantine_time: A list that stores the amount of day of quarantine required before working in a certain city
    :param home: Integer that specifies the starting city of the salesperson
    :return: integer that represents the maximum amount of money that can be earned by a salesperson
    :time complexity: O(nd) where: n is the number of cities,
                                   d is the number of days
    :space complexity: O(nd) where: n is the number of cities,
                                    d is the number of days
    """
    # Setup
    memo = []
    temp = [0] * len(profit[0])
    for _ in range(0, len(profit)):
        memo.append(temp[:])
    memo[-1] = profit[-1]

    # Nested Loop
    for day in range(len(profit) - 2, -1, -1):
        for city in range(0, len(profit[0])):

            # If Stay in place
            memo[day][city] = max((memo[day + 1][city] + profit[day][city]), memo[day][city])

            # If move to left
            counter = 1
            while city - counter >= 0 and counter <= 10:
                left = city - counter
                eligibleDay = day - quarantine_time[city] - counter

                if eligibleDay >= 0:
                    memo[eligibleDay][left] = max((memo[day + 1][city] + profit[eligibleDay][left]),
                                                  memo[eligibleDay][left])
                if eligibleDay == -1:
                    memo[0][left] = max(memo[day + 1][city], memo[0][left])

                counter += 1

            # If move to right
            counter = 1
            while city + counter < len(profit[0]) and counter <= 5:
                right = city + counter
                eligibleDay = day - quarantine_time[city] - counter
                
                if eligibleDay >= 0:
                    memo[eligibleDay][right] = max((memo[day + 1][city] + profit[eligibleDay][right]),
                                                   memo[eligibleDay][right])
                if eligibleDay == -1:
                    memo[0][right] = max(memo[day + 1][city], memo[0][right])

                counter += 1

    return memo[0][home]
