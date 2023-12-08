package controller;

/**
 * Represents all possible actions a player can take in a game of
 * reversi.
 */
public interface PlayerActions {
  /**
   * Allows a player to pass its turn.
   */
  public void pass();

  /**
   * Allows a player to place a tile on the board.
   */
  public void makeMove();
}

