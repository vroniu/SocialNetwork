# Social Network App
REST API designed for CRUD operations for posts on a social network app. The app supports these operations:
 - retrieving all the posts using the `/api/v1/posts` endpoint. This endpoint supports pagination, in order to improve the application performance.
 - retrieving the top 10 posts with highest `viewCount` values via the `api/v1/posts/top10` endpoint.
 - retrieving a specific post by ID using the `/api/v1/posts/<id>` endpoint.
 - creating and updating posts by hitting the `/api/v1/posts` endpoint using POST and PUT methods, respectively.
 - deleting specific posts by ID via the DELETE method at the `/api/v1/posts/<id>` endpoint.

The app is built using the Spring Boot 2.6 framework, and uses H2 in-memory database. Be careful, the database gets wiped each time the application gets restarted!

## Instalation (on Windows)
This projects requires Java 11.
Clone the repo and run this command to install the dependencies.

    ./mvnw install

To run the unit tests, use this command.

    ./mvnw test

Test coverage data will be exported to `target\site\jacoco\index.html`.

You can launch the app locally on port 8080 using this command.

    ./mvnw spring-boot:run

## Additional stuff
You can test the API using Postman by importing the included collection. There is also a Python script to help populate the database, useful for testing the `api/v1/posts/top10` endpoint.
