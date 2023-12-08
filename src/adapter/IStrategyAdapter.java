package adapter;

import java.util.Optional;

import controller.PlayerColor;
import model.HexShape;
import provider.model.ReadonlyReversiModel;

/**
 * Adapts the strategy interface for use with provided strategies.
 */
public interface IStrategyAdapter {
  Optional<HexShape> selectMove(ReadonlyReversiModel board, PlayerColor color);

}
