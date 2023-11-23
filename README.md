# BusTrips
Simple java application for displaying when the next buses will stop on a given station.

### requirement:
Maven is recommended for this project. You can view the official installation steps [here](https://maven.apache.org/install.html).

### Usage:

* compiling:
```sh
mvn compile
  ```
* running:
```sh
mvn exec:java -q -Dexec.mainClass="busTrips.App" -Dexec.args="2 5 absolute"
  ```
example result:
```sh
101: 12:20, 12:25, 12:25, 12:35, 12:40
106: 12:20, 12:21, 12:25, 12:26, 12:35
107: 12:20, 12:25, 12:26, 12:35, 12:41
  ```
