# Railway Traffic Simulator

A client-server `java` application designed to simulate railway traffic, manage train schedules across stations, propagate delays dynamically, and detect track capacity conflicts.

## Project Goals
- **Client-Server Architecture:** Multi-threaded TCP server communicating with console/GUI clients using JSON messages.
- **Database Persistence:** Relational data storage using PostgreSQL (`stations`, `trains`, `train_stops`, `users`, and `conflicts`).
- **Role-Based Access Control:** Separate privileges for `ADMIN` (schedule and infrastructure management) and `OPERATOR` (traffic monitoring and delay injection).
- **Delay Propagation & Conflict Detection:** Automatic recalculation of downstream arrival/departure times and real-time verification of station track occupancy.

## Tech Stack
- **Language:** Java 21
- **Build Tool:** Maven
- **Database:** PostgreSQL 14+ (JDBC)
- **JSON Serialization:** Google Gson
- **Testing:** JUnit 5