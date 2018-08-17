# ticket-service

> Implementation of a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance.
venue.
For example, see the seating arrangement below.
```
 ----------[[ STAGE ]]----------
 ---------------------------------
 sssssssssssssssssssssssssssssssss
 sssssssssssssssssssssssssssssssss
 sssssssssssssssssssssssssssssssss
 sssssssssssssssssssssssssssssssss
 sssssssssssssssssssssssssssssssss
 sssssssssssssssssssssssssssssssss
 sssssssssssssssssssssssssssssssss
 sssssssssssssssssssssssssssssssss
 sssssssssssssssssssssssssssssssss
```
## Provide the following functions

- Find the number of seats available within the venue
- Find and hold the best available seats on behalf of a customer
- Reserve and commit a specific group of held seats for a customer

Assumptions:
- Available seats are seats that are neither held nor reserved.
- Each ticket hold should expire within a set number of seconds.

## Requirements

- The ticket service implementation should be written in Java
- The solution and tests should build and execute entirely via the command line using either Maven or Gradle as the build tool
- A README file should be included in your submission that documents your assumptions and includes instructions for building the solution and executing the tests
- Implementation mechanisms such as disk-based storage, a REST API, and a front-end GUI are not required

## Design and Assumptions

- The ticket service implementation is for standalone usage. The idea is for a separate web service application to use [SimpleTicketService](https://github.com/tili2983/ticket-service/blob/master/src/main/java/service/SimpleTicketService.java) by creating an instance of it.
- The ticket service implementation involves booking or reservation only and billing or payment handling are not included.
- The ticket service provides a simple rating algorithm in [FrontMiddleBetterRater](https://github.com/tili2983/ticket-service/blob/master/src/main/java/rater/FrontMiddleBetterRater.java) to pick the best seats for holding seats but does not guarantee the same seat hold would have seats side by side.
- The same customer (identified by email address) can hold or reserve any number of seats any number of times as long as there is available seats.
- Seat hold expiration is configurable.
- Instead of exposing SeatHold object, SimpleTicketService only return the ID of the SeaHold object to the caller service through the `holdAvailableSeats()` method.
- A cron-job like service should call `removeExpiredSeatHolds()` method to clean up expired seat holds periodically.
- Data storage is considered out of scope. Once the service ends, all seat holds and reservation stored in HashMap objects in SimpleTicketService will be lost.
- A SimpleTicketService instance has a [SimpleSeatingService](https://github.com/tili2983/ticket-service/blob/master/src/main/java/service/SimpleSeatingService.java) instance which manages maintaining and updating the state of the seats of the given Venue object.


## Instructions

### Installing 
```
git clone https://github.com/tili2983/ticket-service.git
cd ticket-service
```

### Building
`mvn clean package`

### Testing
`mvn test`


## Out of Scope
-  Web services
-  REST API
-  Front-end GUI
-  Disk-based storage
-  Billing and payment processing
-  More humanized seating ratings and assignment algorithm

