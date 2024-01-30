#!/bin/bash

my_executable="./reach"
example_executable="./exReach"

failed_test_cases=""
passed_test_cases=""
failed_valgrind_cases=""
passed_valgrind_cases=""

GREEN='\033[0;32m' # Green
RED='\033[0;31m'   # Red
NC='\033[0m'       # No Color

for ((i = 1; i <= 15; i++)); do
    input_file="test$i.txt"
    my_output="myOut$i"
    example_output="exOut$i"
    my_valgrind_output="myValOut$i"
    echo "TEST: $i"
    valgrind $my_executable < $input_file > $my_valgrind_output

    grepVar=$(grep "ERROR SUMMARY: 0 errors" $my_valgrind_output)

    if [ -z "$grepVar" ]; then
        ./$my_executable < $input_file > $my_output
        my_exit_code=$?
        echo $my_exit_code >> $my_output

        ./$example_executable < $input_file > $example_output
        example_exit_code=$?
        echo $example_exit_code >> $example_output

        diff -Z $my_output $example_output
        diff_result=$?

        if [ $diff_result -eq 0 ]; then
            passed_test_cases+="$i "
            passed_valgrind_cases+="$i "
        else
            failed_test_cases+="$i "
            failed_valgrind_cases+="$i "
        fi
    else
        failed_test_cases+="$i "
        failed_valgrind_cases+="$i "
    fi
done

if [ -n "$passed_test_cases" ]; then
    echo -e "${GREEN}PASSED TEST CASES:${NC} $passed_test_cases"
fi

if [ -n "$failed_test_cases" ]; then
    echo -e "${RED}FAILED TEST CASES:${NC} $failed_test_cases"
fi

if [ -n "$passed_valgrind_cases" ]; then
    echo -e "${GREEN}PASSED VALGRIND CASES:${NC} $passed_valgrind_cases"
fi

if [ -n "$failed_valgrind_cases" ]; then
    echo -e "${RED}FAILED VALGRIND CASES:${NC} $failed_valgrind_cases"
fi