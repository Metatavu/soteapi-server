#!/bin/bash

mvn clean verify jacoco:report coveralls:report -Pitests sonar:sonar -DskipCoverage=false -DrepoToken=$COVERALLS_TOKEN