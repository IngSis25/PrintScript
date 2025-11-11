#!/bin/bash
cd /Users/bian/faculty/PrintScript

# Ejecutar solo el test de V1.0
./gradlew :formatter:cleanTest :formatter:test --tests "TCKFormatterTest.testAllTCKCases_V10" --info 2>&1 | grep -E "(✅|❌|💥|Expected:|Got:|Results|Passed:|Failed:)"

