# ChessOnline

## About

ChessOnline is a full-stack application that allows players to play Chess against a friend on the same computer when offline, or play against another player connected to the server. The server and client were created using Spring Boot and React.JS respectively. The backend connects to a PostgreSQL database used for storing user accounts, with Json Web Token (JWT) based authentication.

## Usage

The web application has been deployed online to Github pages. Visit the webpage at https://gordoly.github.io/ChessOnline/.

## Features

- ChessOnline allows multiple users to connect to the server and which will assign each user into pairs, where they will play online Chess.
- Web sockets are used to enable real time game play between users.
- Upon authentication, JWT tokens are generated and distributed to the client.
- A REST api is used to handle requests from the client, where requests are only processed if they contain the user's JWT token.
- PostgreSQL database stores user credentials. User statistics such as their wins and losses are also stored, and aggregated into a leader board for users to view their relative performance.