package provider.controller;

import controller.PlayerColor;
import controller.Strategy;
import provider.model.HexCoordinates;

import java.util.List;
import java.util.Optional;


/**
 * Represents having an n number of strategies, where each strategy is implemented
 * until a move is found.
 */
public class MultipleStrategies implements Strategy {
  private final List<Strategy> e;

  /**
   * The constructor for multiple strategies.
   *
   * @param e A list of strategies.
   */
  public MultipleStrategies(List<Strategy> e) {
    this.e = e;
  }


  /**
   * Determines the hexagonal axial coordinate of the most strategic move on the
   * board for this strategy.
   *
   * @param color The color of the tile of the player using this strategy.
   * @return The most strategic move on the board for this strategy.
   */
  @Override
  public Optional<HexCoordinates> chooseMove(PlayerColor color) {
    isColorEmpty(color);
    for (Strategy s : e) {
      Optional<HexCoordinates> hc = s.chooseMove(color);
      if (hc.isPresent()) {
        return hc;
      }
    }
    return Optional.empty();
  }

  /**
   * determines if the color is empty and throws an exception if so.
   *
   * @param color the color of the player
   */
  private static void isColorEmpty(PlayerColor color) {
    if (color == PlayerColor.EMPTY) {
      throw new IllegalArgumentException("color cannot be empty");
    }
  }
}
