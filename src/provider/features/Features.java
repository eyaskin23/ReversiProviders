package provider.features;

/**
 * Represents all possible requests from a player of Reversi, where
 * a player has the ability to pass their turn or place a tile on the board of
 * the game.
 */
public interface Features {

  /**
   * Passes a player's turn and updates this features' view to show that the player
   * is not placing any tiles in this round.
   */
  public void passMove();


  /**
   * Places a player's tile at the most recently highlighted coordinates in this features'
   * view and updates the view to reflect and flipped tiles.
   */
  public void placeTile();
}