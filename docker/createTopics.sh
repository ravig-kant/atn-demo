#!/bin/bash

bootstrapServer="kafka-broker-1:9092"
topics=(
"step-execution-change-topic"
"public-event-topic"
"command-executor-topic"
)

printf "Creating topics"
for topic in "${topics[@]}"; do
    printf "\nCreating $topic topic\n"
    kafka-topics.sh --bootstrap-server $bootstrapServer --topic "${topic}" --create --partitions 2 --config cleanup.policy=compact --config min.cleanable.dirty.ratio=0.01 --if-not-exists
done