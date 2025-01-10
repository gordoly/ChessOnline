# ChessOnline

## About

ChessOnline is a full-stack application that allows players to play Chess against a friend on the same computer when offline, or play against another player connected to the server. The server was created using Spring Boot while the client was created using React.js. ChessOnline allows multiple users to connect to the server and the server will assign each user into pairs, where they will play online Chess. ChessOnline utilises sessions to save the user's session so they do not need to enter their name when connecting to the server. Furthermore, Websockets is used to carry user chess movers to opponents, facilitating an online experience.

## Usage

Before running the application, ensure you have Java installed on your system. You can check if Java is installed by running:

```bash
java -version
```

To run this application, the compiled program can be found in the root directory of this repository as a .jar file. Run the .jar file using the command:

```bash
java -jar Chess-0.0.1-SNAPSHOT.jar
```

Then, visit the website at localhost:8080.