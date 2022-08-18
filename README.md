# Application : tictactoeapp (Tic Tac Toe Game)
#Rules to play the game:

- 2 players can play the game at a time.
- Players should use the numbers between 0-9 numbers.
- Players one after another should take a move.
- The game is played on a grid that's 3 squares by 3 squares.
- You are X, your friend is O. Players take turns putting their marks in empty squares.
- The first player to get 3 of her marks in a row (up, down, across, or diagonally) is the winner.
- When all 9 squares are full, the game is over. If no player has 3 marks in a row, the game ends in a tie.

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.tictactoe.app.TictactoeappApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

Commmandline : First , execute mvn clean compile  then execute following command in respective directory.

```shell
>java -jar target/tictactoeapp-0.0.1-SNAPSHOT.jar
```