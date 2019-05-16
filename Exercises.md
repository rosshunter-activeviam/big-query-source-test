# Performance Training Course - Exercises

Note: this is the planned list of planned exercises. They may not all be complete and finalised at the moment. This doc may be moved to another format one day.

## Getting started
1. Start up the ActivePivot server


   * Explore the dimensions and measures
   * Start up jConsole, VisualVM and JFR and familiarise yourself with these technologies
   * Run the `MDXBenchmark` class in `test-clients` and observe the results

## Loading Data
### Parsing Threads
1. What value should the parserThreads be for a 32-core machine?

1. Change the value in localpath.properties and reload the data with:

   a) 1 parser thread
   
   b) 16 parser threads
   
   c) 32 parser threads
   
   d) 64 parser threads
   
Note the load time and the differences

### Zip files
1. Load the zipped file. What is the difference in load time? How much space is saved on the file system?

### Multiple files
1. Load the X smaller files. What is the difference in load time?

## Datastore
### Dictionary
1. Remove `RISK_BUCKET__RISKCURRENCY` from the dimensions, start the server and record the memory usage after doing a GC

1. Dictionarise `RISK_BUCKET__RISKCURRENCY` on the datastore, start the server and record the memory usage after doing a GC

Compare the different results - how has the memory usage changed? Why did we need to remove the field before dictionarising it? 

### Partitioning
1. Run up the server and note the loading time.

##### Modulo Partitioning
1. Choose a likely field in the base store to add modulo partitioning on. Run up the server and note the change in loading time. Try using different values for the number of partitions

   a) 4 partitions
  
   b) 16 partitions
  
   c) 32 partitions
  
   d) 64 partitions
   
Note the load time and the differences

##### Value Partitioning
1. Try the same with a value partitioning on a suitable datastore field.

### Chunk Size
Start the server and collect the memory and chunk statistics - find the default chunk size

1. Edit the chunk size, using:

   a) A smaller value
   
   b) A larger value
   
Note the differences in memory usage and chunk statistics

## Aggregate Providers
### No partitioning
1. Alternate between the different providers and measure the:


  * load time
  * memory usage
  * query time
  
Explore what happens if we create partial providers

### With partitioning
Explore the effects of a good and bad partitioning

1. Pick a field which has many values and thus is a good candidate for partitioning. How does it affect the measurable values? How many threads are being used?
1. Pick a field which does have many values and thus is a not a good candidate for partitioning. How does it affect the measurable values? How many threads are being used?

## Aggregates Cache
Testing on a single query
1. Execute a query several times without the cache and note the average query time.
1. Turn on the aggregates cache and execute the query. This should take the same time as before.
1. Execute the query again - it should return almost instantly.


Running the test suite
1. Execute some queries without the cache (hint: use `MdxBenchmark`) and note the average query times for each query
1. Turn on the aggregates cache and run the test again - compare the average query times. How many resources is the server using with the cache on?

## Query Plans
Turning query plans on
1. Execute a query from the UI with the query planning context on
1. Turn on query plans through the mbean

Analyse the logs, viewing the query plans in a text editor and the [online tool](https://activeviam.github.io/queryplan-analyzer/)


Analysing results
1. Observe different query plans depending on the usage of aggregate providers and aggregates cache


## Postprocessors
To be added

## Vectorisation
There is some vectorised data available for you
1. Use the vectorised data profile (to add exact name) to load in the data
1. Observe the difference in load, memory and query times

