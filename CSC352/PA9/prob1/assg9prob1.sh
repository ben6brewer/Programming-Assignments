#!/bin/bash

# Define color codes
green="\e[32m"
red="\e[31m"
yellow="\e[33m" # Yellow color code
reset="\e[0m"

# Arrays to store test cases results
passed_test_cases=()
failed_test_cases=()
passed_valgrind_cases=()
failed_valgrind_cases=()

# Function to run test case
runTestCase() {
    local test_num="$1"
    local target="$2"
    local touch_file="$3"

    touch "$touch_file"
    ./mymake2 -f "makefile$test_num" "$target" > "myOut$test_num" 2>&1
    echo $? >> "myOut$test_num"

    touch "$touch_file"
    ./exMymake2 -f "makefile$test_num" "$target" > "exOut$test_num" 2>&1
    echo $? >> "exOut$test_num"

    if diff -Z "myOut$test_num" "exOut$test_num"; then
        passed_test_cases+=("$test_num")
    else
        failed_test_cases+=("$test_num")
    fi

    touch "$touch_file"
    valgrind_output="${test_num}_valgrind"
    valgrind --leak-check=full --show-leak-kinds=all ./mymake2 -f "makefile$test_num" "$target" > "$valgrind_output" 2>&1
    echo $? >> "$valgrind_output"

    if grep -q "ERROR SUMMARY: 0" "$valgrind_output" && grep -q "All heap blocks were freed -- no leaks are possible" "$valgrind_output"; then
        passed_valgrind_cases+=("$valgrind_output")
    else
        failed_valgrind_cases+=("$valgrind_output")
    fi
}

# TEST NUMBER, TARGET, TOUCH
runTestCase "1" "A" "F"
runTestCase "2" "C" "F"
runTestCase "3" "C" "F"
runTestCase "4" "A" "D"
runTestCase "5" "A" "F"

# Calculate percentages
total_test_cases=$(( ${#passed_test_cases[@]} + ${#failed_test_cases[@]} ))
total_valgrind_cases=$(( ${#passed_valgrind_cases[@]} + ${#failed_valgrind_cases[@]} ))

# Calculate percentages
passed_test_percentage=$(( ${#passed_test_cases[@]} * 100 / total_test_cases ))
passed_valgrind_percentage=$(( ${#passed_valgrind_cases[@]} * 100 / total_valgrind_cases ))

# Calculate the average
expected_grade_percentage=$(( (passed_test_percentage + passed_valgrind_percentage) / 2 ))

# Print the test case results with colored text
echo -e "${green}PASSED TEST CASES:${reset}"
for testcase in "${passed_test_cases[@]}"; do
    echo "myOut$testcase"
done

echo -e "${red}FAILED TEST CASES:${reset}"
for testcase in "${failed_test_cases[@]}"; do
    echo "myOut$testcase"
done

# Print the Valgrind results with colored text
echo -e "${green}PASSED VALGRIND CASES:${reset}"
for testcase in "${passed_valgrind_cases[@]}"; do
    echo "$testcase"
done

echo -e "${red}FAILED VALGRIND CASES:${reset}"
for testcase in "${failed_valgrind_cases[@]}"; do
    echo "$testcase"
done

# Print passed percentages
echo -e "\n${yellow}STDOUT: $passed_test_percentage%${reset}"
echo -e "${yellow}VALGRIND: $passed_valgrind_percentage%${reset}"

# Print expected grade
echo -e "${yellow}EXPECTED GRADE: $expected_grade_percentage%${reset}"
