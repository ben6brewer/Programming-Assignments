#!/bin/bash

# Define color codes
green="\e[32m"
red="\e[31m"
yellow="\e[33m" # Yellow color code
reset="\e[0m"

# Define the directories and executable names
testcase_dir="testcases"
my_executable="./mymake"
example_executable="./exMymake"

# Arrays to store test cases results
passed_test_cases=()
failed_test_cases=()
passed_valgrind_cases=()
failed_valgrind_cases=()

# Initialize a variable to keep track of the total number of test cases
total_cases=0

# Loop through all files in the testcase directory
for testcase_file in "$testcase_dir"/*; do
    # Get the base name of the file without the path and extension
    testcase_name=$(basename "$testcase_file" | cut -d. -f1)

    # Extract the objectName from the last string after the underscore
    object_name=$(echo "$testcase_name" | awk -F_ '{print $NF}')

    # Execute your program and save the output
    my_output="${testcase_name}_my_output.txt"
    $my_executable "$testcase_file" "$object_name" > "$my_output"
    my_return_value=$?
    echo "Return Value: $my_return_value" >> "$my_output"

    # Execute the example program and save the output
    example_output="${testcase_name}_example_output.txt"
    $example_executable "$testcase_file" "$object_name" > "$example_output"
    example_return_value=$?
    echo "Return Value: $example_return_value" >> "$example_output"

    # Compare the output files and add to the appropriate test case array
    if diff -q -w "$example_output" "$my_output" &>/dev/null; then
        passed_test_cases+=("$testcase_name")
    else
        failed_test_cases+=("$testcase_name")
    fi

    # Execute your program with valgrind and save the output
    valgrind_output="${testcase_name}_valgrind_output.txt"
    valgrind $my_executable "$testcase_file" "$object_name" > "$valgrind_output" 2>&1
    valgrind_return_value=$?

    # Check if the valgrind output contains both "ERROR SUMMARY: 0" and "all heap blocks were freed"
    if grep -q "ERROR SUMMARY: 0" "$valgrind_output" && grep -q "All heap blocks were freed -- no leaks are possible" "$valgrind_output"; then
        passed_valgrind_cases+=("$valgrind_output")
    else
        failed_valgrind_cases+=("$valgrind_output")
        echo -e "${red}Valgrind FAILED for $testcase_name${reset}"
        cat "$valgrind_output"
    fi

    # Increment the total number of test cases
    ((total_cases++))
done

# Calculate the test cases and Valgrind pass percentages
test_cases_percentage=$(bc <<< "scale=2; (${#passed_test_cases[@]} / $total_cases) * 100")
valgrind_percentage=$(bc <<< "scale=2; (${#passed_valgrind_cases[@]} / $total_cases) * 100")

# Print the test case results with colored text
echo -e "${green}PASSED TEST CASES:${reset}"
for testcase in "${passed_test_cases[@]}"; do
    echo "$testcase"
done

echo -e "${red}FAILED TEST CASES:${reset}"
for testcase in "${failed_test_cases[@]}"; do
    echo "$testcase"
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

# Print the test case and Valgrind pass percentages
echo -e "${yellow}stdout         : ${#passed_test_cases[@]}/$total_cases = $test_cases_percentage%${reset}"
echo -e "${yellow}Valgrind       : ${#passed_valgrind_cases[@]}/$total_cases = $valgrind_percentage%${reset}"

# Calculate and print the estimated score
total_passed=$(( ${#passed_test_cases[@]} + ${#passed_valgrind_cases[@]} ))
average_percentage=$(bc <<< "scale=2; ($total_passed / (2 * $total_cases)) * 100")
echo -e "${yellow}Estimated Score: $total_passed/$((2 * $total_cases)) = $average_percentage%${reset}"


