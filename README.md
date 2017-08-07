## Statistics API

Statistics API is a thread-safe, Jetty-backed application that processes basic statistics within a sliding window.

### Assumptions
1. Data ingestion on the _/transactions_ endpoint is a blocking operation.
2. The _/statistics_ endpoint can handle concurrent requests and executes in constant time and space.

### Approach
1. The sliding window is assumed to be 60 seconds.
2. Incoming statistical data is aggregated and maintained in a `StatisticsAggregateData` object for each distinct second in the sliding window.
3. A map, with a number of entries equal to the time interval (60), is used to hold aggregated data for the entire sliding window. More entries could be present in cases where stale entries have not been cleaned up.
4. A ConcurrentHashMap is used to help guarantee atomic operations- chosen over a synchronized map which would have entailed locks on the entire collection.
5. [Spark](http://sparkjava.com/) web framework chosen to define the API handlers.

### Installation
* Prerequisites:
    + Java 8
    + Maven 3
    
    
* On Ubuntu 16.04 LTS

    ~~~
    $ sudo apt-get update
    $ sudo apt-get install maven
    $ sudo apt-get install default-jre default-jdk 
    ~~~

* On OS X El Capitan

    ~~~
    $ brew update
    $ brew install maven
    $ brew cask install java
    ~~~
    
* Clone the repository from github and run the API server:

~~~
git clone git@github.com:pandora/Statistics-API.git
cd Statistics-API
mvn clean
mvn compile
mvn package
java -cp target/sapi-0.0.1.jar me.thomas.sapi.Application
~~~

### API Reference
* /transactions
    * Method:
        + POST
    * Data Params: 
        + amount (double)
        + timestamp (long)
    * Success Response
        + 201 Created
    * Error Response
        + 204 No Content (stale transaction not inserted)


* /statistics
    * Method:
        + GET
    * Success Response
        + 200 OK
    * Error Response
        + 500 Internal Server Error (no explicit error codes defined yet)

# Tests
1. To explicitly execute tests, run `mvn test` on the route folder.
2. The `ConcurrentTestRunner` from the [vmlens](https://mvnrepository.com/artifact/com.vmlens/concurrent-junit) library is used to test logic that is intended to be concurrent.
