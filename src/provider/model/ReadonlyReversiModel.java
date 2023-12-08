package provider.model;

import controller.PlayerColor;

import java.util.HashMap;

/**
 * Represents a primary model interface for a read only model
 * of reversi, where a client may only access observation methods
 * of the model, and is unable to modify the model in any way.
 */
public interface ReadonlyReversiModel {
  /**
   * Determines if the given player color has a valid move
   * on this model's board.f
   * @param color   A player color of a tile of this model.
   * @return   true iff the given color has any valid moves left of
   *           this model's board.
   */
  public boolean hasValidMove(PlayerColor color);


  /**
   * Provides the status of this reversi model,
   * which is one of: TIE, PLAYING, W_WON, B_WON.
   *
   * @return the status of this reversi model.
   */
  public GameState getStatus();

  /**
   * Provides a copy of the board of this reversi model.
   *
   * @return the board of this reversi model.
   */
  public HashMap<HexCoordinates, PlayerColor> getBoard();

  /**
   * Creates a board of the given size in a default initial state.
   *
   * @return a board of the given size in default initial state.
   */
  public HashMap<HexCoordinates, PlayerColor> defaultBoard(int size);

  /**
   * Returns the size of the board of this reversi model.
   */
  public int getSizeOfBoard();

  /**
   * Returns the contents of a cell at a given coordinate in this reversi model,
   * where the content can be one of WHITE, BLACK, EMPTY.
   *
   * @return the PlayerColor at the given q,r axial coordinate in this reversi model.
   */
  public PlayerColor getContentsOfCell(int q, int r);

  /**
   * Returns the current score for the given player color.
   *
   * @return the total sum of tiles with the given color on the board of this reversi model.
   */
  public int getScore(PlayerColor color);

  /**
   * Determines whether the game is over for this reversi model.
   *
   * @return true iff the GameState for this reversi model is either PLAYING, W_WON, B_WON
   */
  public boolean isGameOver();

  /**
   * Determines if there is a HexCoordinate on this model's board at the given axial q,r
   * coordinates.
   * @param q   A axial q-coordinate.
   * @param r   A axial r-coordinate.
   * @return    true iff the given axial coordinates are a HexCoordinate within this model's board.
   */
  public boolean isValidCoordinate(int q, int r);

  /**
   * determines if placing the tile at the given coordinates is a valid move for the player.
   * @param row the row coordinate (q) at which the user is attempting to put the tile
   * @param col the column coordinate (r) at which the user is attempting to put the tile
   * @param color the color of the current player
   * @return true iff the move is a valid move
   */
  public boolean isValidMove(int row, int col, PlayerColor color);


  /**
   * Determines the current PlayerColor which is able to place a tile on this model's board.
   * @return    The PlayerColor that is able to add a tile to the board in this round.
   */
  public PlayerColor curPlayer();

  /**
   * Determines how many tiles would be captured if the given PlayerColor places a tile
   * at the given q,r coordinates on the board.
   * @param q  the q coordinate on the board.
   * @param r  the r coordinate on the board.
   * @param color     The color of a tile.
   * @return  the number of tiles that are captured if the given player placed a tile at the given
   *          q,r coordinate.
   */
  public int numTilesCaptured(int q, int r, PlayerColor color);
}
