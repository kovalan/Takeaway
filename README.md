# Game of Three

Consists of Server program and the client program

1. game-of-three (server)
2. game-of-three-client (client)

This game can be run in both auto mode and manual mode.  In order to run in manual mode, run the game-of-three-client with "true" as a run time program argument.

## STEPS
1. Run game-of-three (server) first.
2. Run game-of-three-client two times, one program for each player.
3. If a client is run in manual mode, provide input in the console to continue with the game.

## Note:
1. Though the program is designed for more than 2 players, it is not tested with more than 2 players.
2. There is scope for further improvisation as currently the client program checks the server for status.  Instead we can make the server notify the client for any status change.
3. "GameOfThreeResponse.java" class is repeated in both game-of-three and game-of-three-client.  Better approach would be to create a lib for model classes and share between the two programs.


