#!/bin/bash

mvn clean verify jacoco:report coveralls:report -Pitests sonar:sonar -DskipCoverage=false -DrepoToken=$COVERALLS_TOKEN --batch-mode -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn
