package adapter;

import java.util.HashMap;

import provider.model.HexCoordinates;
import provider.model.ReadonlyReversiModel;
import provider.model.ReversiModel;
import controller.DirectionsEnum;
import controller.PlayerColor;
import controller.PlayerType;
import model.Board;
import model.HexShape;

/**
 * Adapts the model interface for use in implementing the view.
 */
public class ModelImpl implements ReadonlyReversiModel, ReversiModel {
  Board board;

  public ModelImpl() {
    this.board = new Board();
  }

  @Override
  public boolean hasValidMove(PlayerColor color) {
    return board.isValidMove(0, 0, convertColor(color));
  }

  @Override
  public provider.model.GameState getStatus() {
    if (!(board.isGameOver())) {
      return provider.model.GameState.PLAYING;
    }
    if (board.isGameOver() && board.getScoreBlack() >
            board.getScoreWhite()) {
      return provider.model.GameState.B_WON;
    }
    if (board.isGameOver() && board.getScoreBlack() <
            board.getScoreWhite()) {
      return provider.model.GameState.W_WON;
    } else if (board.isGameOver() && board.getScoreBlack() ==
            board.getScoreWhite()) {
      return provider.model.GameState.TIE;
    }
    return null;
  }

  @Override
  public HashMap<HexCoordinates, PlayerColor> getBoard() {
    HashMap<HexCoordinates, PlayerColor> convertedBoard = new HashMap<>();
    for (int i = 0; i < board.getBoardSize(); i++) {
      for (int j = 0; j < board.getBoardSize(); j++) {
        HexShape hex = board.getCurrentHex(i, j);
        if (hex != null) {
          HexCoordinates coordinates = new HexCoordinates(i, j);
          PlayerColor color = convertType(hex.getPlayerType());
          convertedBoard.put(coordinates, color);
        }
      }
    }
    return convertedBoard;
  }

  @Override
  public HashMap<provider.model.HexCoordinates, PlayerColor> defaultBoard(int size) {
    return null;
  }

  @Override
  public int getSizeOfBoard() {
    return board.getBoardSize();
  }

  @Override
  public PlayerColor getContentsOfCell(int q, int r) {
    if (isValidCoordinate(q, r)) {
      HexShape hex = board.getCurrentHex(q + board.getBoardSize() / 2 - 1,
              r + board.getBoardSize() / 2 - 1);
      if (hex != null) {
        return convertType(hex.getPlayerType());
      }
    }
    return PlayerColor.EMPTY;
  }

  @Override
  public int getScore(PlayerColor color) {
    return board.countPieces(convertColor(color));
  }

  @Override
  public boolean isGameOver() {
    return board.isGameOver();
  }

  @Override
  public boolean isValidCoordinate(int q, int r) {
    return board.isValidCoordinate(q, r);
  }


  @Override
  public void addFeatures(provider.model.FeaturesModel f) {
    // This method is already implemented by calling the view in the main.
    // But it's needed to implement so that the interface doesn't throw errors.
    //
  }


  @Override
  public void startGame() {
    // This method is already implemented by calling the view in the main.
    // But it's needed to implement so that the interface doesn't throw errors.
  }

  @Override
  public void checkGame() {
    if (board.isGameOver()) {
      board.notifyObserversGameOver();
    }
  }

  @Override
  public void placeTile(PlayerColor color, int q, int r) throws IllegalArgumentException {
    board.placePiece(q, r, convertColor(color));
  }

  @Override
  public void pass(PlayerColor color) {
    if (isGameOver()) {
      throw new IllegalStateException("Game is over.");
    }
    if (!hasValidMove(color)) {
      if (color == PlayerColor.WHITE) {
        board.resetWhitePassed();
      } else if (color == PlayerColor.BLACK) {
        board.resetBlackPassed();
      }
      if (board.hasPlayerPassed(PlayerType.WHITE)
              && board.hasPlayerPassed(PlayerType.BLACK)) {
        if (board.getScoreBlack() > board.getScoreWhite()) {
          setStatus(provider.model.GameState.B_WON);
        } else if (board.getScoreBlack() < board.getScoreWhite()) {
          setStatus(provider.model.GameState.W_WON);
        } else if (board.getScoreBlack() == board.getScoreWhite()) {
          setStatus(provider.model.GameState.TIE);
        } else {
          setStatus(provider.model.GameState.PLAYING);
        }
      } else {
        throw new IllegalStateException("The player has valid moves left.");
      }
    }
  }

  private void setStatus(provider.model.GameState gameState) {
    // This method is already implemented by calling the view in the main.
    // But it's needed to implement so that the interface doesn't throw errors.
  }

  @Override
  public boolean isValidMove(int row, int col, PlayerColor color) {
    int q = col + board.getBoardSize() / 2;
    int r = row + board.getBoardSize() / 2;

    if (!isValidCoordinate(q, r) || board.getCurrentHex(r, q).getPlayerType() != PlayerType.EMPTY) {
      return false;
    }

    PlayerType playerType = convertColor(color);
    PlayerType opponent = playerType.nextPlayer();

    for (DirectionsEnum dir : DirectionsEnum.values()) {
      int nextQ = q + dir.getQMove();
      int nextR = r + dir.getRMove();

      if (!isValidCoordinate(nextQ, nextR)) {
        continue;
      }
      HexShape neighborHex = board.getCurrentHex(nextR, nextQ);
      if (neighborHex == null) {
        continue;
      }

      if (neighborHex.getPlayerType() != opponent) {
        continue;
      }

      nextQ += dir.getQMove();
      nextR += dir.getRMove();

      while (isValidCoordinate(nextQ, nextR)
              && board.getCurrentHex(nextR, nextQ) != null
              && board.getCurrentHex(nextR, nextQ).getPlayerType() == opponent) {
        nextQ += dir.getQMove();
        nextR += dir.getRMove();
      }

      if (isValidCoordinate(nextQ, nextR) && board.getCurrentHex(nextR, nextQ) != null
              && board.getCurrentHex(nextR, nextQ).getPlayerType() == playerType) {
        return true;
      }
    }
    return false;
  }

  @Override
  public PlayerColor curPlayer() {
    PlayerType current = board.getCurrentTurn();
    return convertType(current);
  }

  @Override
  public int numTilesCaptured(int q, int r, PlayerColor color) {
    board.placePiece(q, r, convertColor(color));
    return board.calculateCaptures(q, r, convertColor(color), board);
  }

  /**
   * Converts their PlayerType class to my PlayerColor class.
   */
  public static PlayerColor convertType(PlayerType type) {
    switch (type) {
      case WHITE:
        return PlayerColor.WHITE;
      case BLACK:
        return PlayerColor.BLACK;
      default:
        return PlayerColor.EMPTY;
    }
  }

  /**
   * Converts their PlayerColor class to my PlayerType class.
   */
  public static PlayerType convertColor(PlayerColor color) {
    switch (color) {
      case WHITE:
        return PlayerType.WHITE;
      case BLACK:
        return PlayerType.BLACK;
      default:
        return PlayerType.EMPTY;
    }
  }
}
