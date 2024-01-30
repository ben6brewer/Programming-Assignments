#!/bin/bash

# Set the paths to your executables
my_executable="./strmath"
example_executable="./exStrmath"

# Initialize lists for failed and passed test cases
failed_cases=""
passed_cases=""

# Loop through test cases from prob1test1.txt to prob1test13.txt
for ((i = 1; i <= 19; i++)); do
    input_file="test$i.txt"
    my_output="myOut$i"
    example_output="exOut$i"

    # Run your executable and capture its output
    $my_executable < $input_file > $my_output
    echo $? >> "myOut$i"

    # Run the example executable and capture its output
    $example_executable < $input_file > $example_output
    echo $? >> "exOut$i"

    # Compare the outputs using diff
    diff_result=$(diff $my_output $example_output)

    if [ -z "$diff_result" ]; then
        passed_cases+="$i "
    else
        failed_cases+="$i "
    fi
done

# Print the results

if [ -n "$passed_cases" ]; then
    echo "PASSED CASES: $passed_cases"
fi

if [ -n "$failed_cases" ]; then
    echo "FAILED CASES: $failed_cases"
fi