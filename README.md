# ChessOnline

ChessOnline is a full-stack application that allows players to play Chess against a friend on the same computer when offline, or play against another player connected to the server. The server was created using Spring Boot while the client was created using React.js. ChessApp allows multiple users to connect to the server and the server will assign each user into pairs, where they will play online Chess. ChessApp utilises sessions to save the user's session so they do not need to enter their name when connecting to the server. Furthermore, Websockets is used to carry user chess movers to opponents, facilitating an online experience.

To run this application, the compiled program can be found in the root directory of Chess as a .jar file. Run the .jar file and visit the website at localhost:8080.
