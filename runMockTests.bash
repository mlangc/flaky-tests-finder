#!/bin/bash

TESTS_OK=false
MOCK_OUTPUT_BASE_PATH="src/test/resources/com/github/mlangc/flaky/tests/finder"

if [[ $(($RANDOM % 2)) -eq 0 ]]; then
  TESTS_OK=true
fi

if [[ $TESTS_OK == "true" ]]; then
  cat "${MOCK_OUTPUT_BASE_PATH}/testsOkOut.txt"
else
  NOK_IND=$(($RANDOM % 4 + 1))
  cat "${MOCK_OUTPUT_BASE_PATH}/testsNokOut${NOK_IND}.txt"
  exit 1
fi
