# Pipelined Priority Queue

## Task

Implement a thread-safe priority queue that performs better than Java's PriorityBlockingQueue under concurrent access.

## Build Instructions

### Prerequisites

* Java 1.7+
* Maven

### Run commands

1. mvn package
2. cd target/
3. java -jar threadsafe-pq-1.0-SNAPSHOT-jar-with-dependencies.jar < PATH_TO_INPUT_FILE


## Usage instructions

To create a new instance of a PipelinedPriorityQueue simply instantiate it like any other queue, example shown below.

```
BlockingQueue<Integer> queue = new PipelinedPriorityQueue<Integer>();
queue.put(1);

Integer head = queue.poll();

```

## Running performance tests

To run the performance tests, remove the @Ignore annotation in the ./src/test/threadsafepq/parallel/BenchmarkTest.java class.

Then, invoke the 'mvn package' again in the root of this repository.