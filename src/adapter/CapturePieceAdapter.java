package adapter;

import java.util.Optional;

import controller.PlayerColor;
import model.HexShape;
import provider.model.ReadonlyReversiModel;

import static adapter.ModelImpl.convertColor;

/**
 * Adapts the CaptureStrategy using providers code.
 * Used to implement it into my command adapter class.
 */
public class CapturePieceAdapter implements IStrategyAdapter {

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

  /**
   * Selects the Move for the capturePiece strategy.
   */
  public Optional<HexShape> selectMove(ReadonlyReversiModel board, PlayerColor color) {
    isColorEmpty(color);
    HexShape bestMove = null;
    int maxCaptured = 0;
    int size = board.getSizeOfBoard();
    for (int q = -size + 1; q < size; q++) {
      for (int r = Math.max(-size + 1, -size - q); r <= Math.min(size - 1, size - q - 1); r++) {
        if (Math.abs(q) + Math.abs(r) + Math.abs(-q - r) <= (size - 1)
                && board.getContentsOfCell(q, r) == PlayerColor.EMPTY
                && board.isValidMove(q, r, color)) {
          int numCaptured = board.numTilesCaptured(q, r, color);
          if (numCaptured > maxCaptured || numCaptured == maxCaptured && bestMove == null) {
            maxCaptured = numCaptured;
            bestMove = new HexShape(q, r, convertColor(color));
          }
        }
      }
    }
    if (bestMove == null) {
      return Optional.empty();
    }
    return Optional.of(bestMove);
  }
}
