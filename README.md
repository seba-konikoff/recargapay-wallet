# recargapay-wallet
Wallet management service for RecargaPay

## Dependencies
* Java 24
* Podman
* Podman-Compose

## Installation instructions

Download the repo and run the `build.sh` script, it will set up the container with the service and database. End the process with ctrl+c to terminate the application

## Testing

Run the script `test.sh` to run all tests

## Design choices

### Infrastructure

Podman was chosen for its ease of use and variety of platforms supported.

The service was designed as stateless, with all pertinent data stored in the database, to ensure an easy transition to a kubernetes-based cloud solution should a pod-based deployment be required to meet demand.

### Application

The application code has few moving parts, as long as the connection between the service and database remains stable there should be no downtime assuming normal traffic. 

To ensure data consistency, all methods that executed multiple changes to the database were marked as transactional, ensuring all changes would be applied, or rollbacked in case of failure.

In order to speed up development and testing, it was decided not to implement OOP patterns for the Wallet, adopting a more procedural approach. This allowed all relevant logic to cohabit, allowing for simpler testing and ease of reading. 

### Tradeoffs

For time, a full transaction trace was not implemented, with the wallet transaction being sufficient for a first version. A transfer log would be the next thing to implement, detailing origin and destination wallets.

Error handling remains basic, all known business failure cases are mapped to exception and have a proper message informing the user of what happened, but should be mapped to a proper response in order to avoid exposing sensitive information.

## Time analysis

In total, the project took approximately 10 hours

* 4 hours on 11/8
* 3 hours on 12/8
* 3 hours on 13/8

This is greater than the guideline outlined in the objectives. Figuring out podman, podman-compose, setting up hibernate, configuring the automated build, and getting the service running all together and in my IDE took most of the development time, since they were new technologies and took considerable learning. Coding the actual service took less time than expected.

Some time was spent getting distracted by my cat.