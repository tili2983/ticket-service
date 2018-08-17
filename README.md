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

## Assumptions and Design

- 

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

