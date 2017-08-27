#!/bin/bash

#java -cp ./bin cs475.Classify
java -cp lib/library.jar:lib/commons-cli-1.2.jar:./bin cs475.Classify -mode train -algorithm ska -model_file output/speech.model -data data/corpus.txt -use_stoplist true -weighting_scheme tf-idf -clustering_training_iterations 5 -num_clusters 10
