# Shogun Board Game

Shogun is a strategic board game played by two players on an 8x8 chessboard, combining elements of chess and checkers. Each player controls a set of pieces, including a king and seven pawns. What distinguishes Shogun is the introduction of **energy**, which determines the movement capabilities of each piece, adding a layer of strategy to the game.

## Features

- **Piece Energy**: Pawns have energy values between 1 and 4, while kings have energy values between 1 and 2. The energy determines how far a piece can move and can change during the game.
  
- **Dynamic Movement**: Pieces move in straight lines (up, down, left, right) or in L-shape moves, with a single 90° turn. Diagonal moves are not allowed, and pieces cannot go forward and then backward in a single move.
  
- **Capture and Block**: Pieces cannot jump over other pieces or stack on their own kind. Capturing is achieved by moving a piece to an occupied square and removing the opponent’s piece from the board.

## Implementation

This repository contains an implementation of the Shogun board game’s piece movement functions. It includes data structures for:
- **Positions**: Coordinates of each piece on the board.
- **Colors**: Two colors (Red and White) representing the two players.
- **Pieces**: The King and Pawn pieces, each with energy values that affect their movement.

Additionally, there are functions for incrementing and decrementing coordinates, allowing for dynamic piece movement.

## Usage

To play the Shogun Board Game, begin by creating an initial board configuration using the `Board` class and the provided piece instances. Here is an example:

```scala
val b_init = Board(Set(
    King(2, Wht, (4,1)), King(1, Red, (5,8)),
    Pawn(4, Wht, (1,1)), Pawn(4, Red, (1,8)),
    // ... (additional piece configurations)
    Pawn(2, Wht, (8,1)), Pawn(3, Red, (8,8))
))
```

Next, you can find all legal moves for a specific piece using the `legal_moves` function. For example:

```scala
val pw1 = Pawn(4, Wht, (4,6))
legal_moves(pw1, b_init)
```

This will return a list of legal moves for the specified piece based on the current board configuration.

## Explore the Game

This repository allows you to dive into the strategic depth of the Shogun board game. By managing your piece energy and carefully planning your moves, you can outsmart your opponent and claim victory.
