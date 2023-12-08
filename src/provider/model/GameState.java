package provider.model;

/**
 * Represents the possible game states for a reversi game, where
 * the state can be PLAYING, B_WON if game is over and there are more
 * black tiles than white tiles on the board, W_WON if game is over and
 * there are more white tiles than black tiles on the board, or TIE
 * if there are an equal number of both tiles on the board.
 */
public enum GameState {
  PLAYING,
  B_WON,
  W_WON,
  TIE
}