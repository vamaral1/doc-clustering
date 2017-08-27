#!/bin/bash

javac -cp ".:lib/library.jar:lib/commons-cli-1.2.jar" -d ./bin cs475/Predictor.java cs475/RegressionLabel.java cs475/ClassificationLabel.java cs475/Classify.java cs475/Similarity.java
