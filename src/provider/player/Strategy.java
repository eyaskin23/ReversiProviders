package provider.player;

import java.util.Optional;

import provider.model.HexCoordinates;

/**
 * Represents a possible strategy a player can use in the game of reversi.
 */
public interface Strategy {

  /**
   * Determines the hexagonal axial coordinate of the most strategic move on the
   * board for this strategy.
   *
   * @param color The color of the tile of the player using this strategy.
   * @return The most strategic move on the board for this strategy.
   */
  public Optional<HexCoordinates> chooseMove(PlayerColor color);
}
