package adapter;

import controller.AIPlayer;
import controller.MoveHandler;
import controller.Player;
import controller.PlayerType;
import model.Board;
import provider.model.ReadonlyReversiModel;
import provider.features.Features;
import provider.view.ReversiFrame;
import view.PlayerActionListener;

/**
 * Adapts the provided controller using my own interfaces.
 */
public class ReversiControllerAdapter implements PlayerActionListener, Features, MoveHandler {
  ReversiFrame frame;
  ReadonlyReversiModel model;
  Board board;
  Player player;

  /**
   * Constructor for implementing the controller adapter.
   */
  public ReversiControllerAdapter(Player player, ReadonlyReversiModel model, ReversiFrame frame) {
    this.player = player;
    this.frame = frame;
    this.model = model;
  }

  @Override
  public void passMove() {
    board.playerPass(board.getCurrentTurn());
  }

  @Override
  public void placeTile() {
    board.placePiece(0,0,convertType(board.getCurrentTurn()));
  }

  public void update() {
    frame.updateBoard();
  }

  @Override
  public void handleMove(Player player, int row, int column) {
    this.placeKey(row, column);
  }

  /**
   * Converts my PlayerType class to their PlayerColor class.
   */
  public static PlayerType convertType(PlayerType type) {
    switch (type) {
      case WHITE:
        return PlayerType.WHITE;
      case BLACK:
        return PlayerType.BLACK;
      default:
        return PlayerType.EMPTY;
    }
  }

  private void placeKey(int x, int y) {

    if (!board.isValidMove(x, y, convertType(player.getType()))) {
      frame.showInvalidMoveMessage();
      return;
    }
    if (x > board.getBoardSize() / 2 || x < -board.getBoardSize()
            || y > board.getBoardSize() / 2 || y < -board.getBoardSize()) {
      frame.showInvalidMoveMessage();
      return;
    }
    int q = x + board.getBoardSize() / 2;
    int r = y + board.getBoardSize() / 2;
    board.placePiece(q, r, player.getType());
    board.flipPieces(q, r, player.getType());
  }

  @Override
  public void onPlayerMove(int row, int column) {
    if (board.isValidMove(row, column, player.getType())) {
      this.placeKey(row, column);
      player.resetHasPassed();
    } else {
      frame.showInvalidMoveMessage();
    }
  }

  @Override
  public void onPass() {
    if (!(player instanceof AIPlayer)) {
      board.playerPass(player.getType());
    }
    player.setHasPassed();
  }
}
