package adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import controller.PlayerColor;
import model.HexShape;
import provider.model.ReadonlyReversiModel;

import static adapter.ModelImpl.convertColor;

/**
 * Adapts the Corners Strategy for use in my CommandAdapter class.
 */
public class CheckCornersAdapter implements IStrategyAdapter {

  /**
   * determines if the color is empty and throws an exception if so.
   * @param color the color of the player
   */
  private static void isColorEmpty(PlayerColor color) {
    if (color == PlayerColor.EMPTY) {
      throw new IllegalArgumentException("color cannot be empty");
    }
  }

  @Override
  public Optional<HexShape> selectMove(ReadonlyReversiModel board, PlayerColor color) {
    isColorEmpty(color);
    HexShape bestMove = null;
    int maxCaptured = 0;
    int size = Math.floorDiv(board.getSizeOfBoard(), 2);
    ArrayList<Integer> coordsToCheck = new ArrayList<>(Arrays.asList(-size, 0, size));
    for (int i = 0; i < coordsToCheck.size(); i++) {
      int q = coordsToCheck.get(i);
      for (int r : coordsToCheck) {
        if (q != r) {
          int numCaptured = board.numTilesCaptured(q, r, color);
          if (numCaptured > maxCaptured || numCaptured
                  == maxCaptured && bestMove == null
                  && board.isValidMove(q, r, color)) {
            maxCaptured = numCaptured;
            bestMove = new HexShape(q, r, convertColor(color));
          }
        }
      }
    }
    if (bestMove == null) {
      IStrategyAdapter s = new CapturePieceAdapter();
      return s.selectMove(board, color);
    }
    return Optional.of(bestMove);
  }
}
