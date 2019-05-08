#!/usr/bin/env bash

./gradlew clean :library:build :library:bintrayUpload $@
