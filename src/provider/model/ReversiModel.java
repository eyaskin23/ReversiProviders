package provider.model;


import controller.PlayerColor;

/**
 * Represents the primary model interface for playing a game of
 * reversi, where a game allows players to place tiles on the board
 * or pass and let the next player move. Players are not allowed to make
 * more than one move and one round, and if both players pass in a row,
 * the game is over.
 */
public interface ReversiModel extends ReadonlyReversiModel {

  /**
   * Adds the given feature to this model.
   *
   * @param f A feature to add to this model.
   */
  public void addFeatures(FeaturesModel f);

  /**
   * Starts this model's game by checking to determine which player has the first
   * turn.
   */
  public void startGame();

  /**
   * Checks to see if anyone has won the game and updates the view accordingly.
   */
  public void checkGame();

  /**
   * Places a tile of the given color at the given q,r axial coordinates on
   * this models board. A tile will only be placed if it is being placed
   * at valid q,r coordinates and if it sandwiches a run of opposite colored
   * tiles with another tile of the same color. Placing a tile will flip any
   * tiles which are of the opposite color between the placed tile and any tile
   * of the same color on any diagonal of the board.
   *
   * @param color represents the color of the player tile
   * @param q     the q axial coordinate of the tile
   * @param r     the r axial coordinate of the tile
   * @throws IllegalArgumentException iff the given coordinates are not valid
   *                                  coordinates on the board.
   * @throws IllegalStateException    iff the move is not a valid move.
   */
  public void placeTile(PlayerColor color, int q, int r) throws IllegalArgumentException;

  /**
   * If the given color has no valid moves left on the board, increments
   * the number of times the player has passed in this round. Increments
   * the number of moves made in this round to ensure the given color cannot
   * go more than once in a row. Checks to see if both players have passed twice
   * in a row, if so, set this model's status to win or tie.
   *
   * @param color The color of a tile on the board.
   * @throws IllegalStateException if the game is already over.
   */
  public void pass(PlayerColor color);

  /**
   * determines if placing the tile at the given coordinates is a valid move for the player.
   *
   * @param row   the row coordinate (q) at which the user is attempting to put the tile
   * @param col   the column coordinate (r) at which the user is attempting to put the tile
   * @param color the color of the current player
   * @return true iff the move is a valid move
   */
  public boolean isValidMove(int row, int col, PlayerColor color);
}