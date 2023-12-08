package adapter;

import java.util.Optional;

import controller.IPlayer;
import controller.Player;
import controller.PlayerType;
import controller.TurnAIPopUp;
import model.HexShape;
import model.ReadOnlyBoardModel;
import provider.model.ReadonlyReversiModel;

import static java.lang.Integer.parseInt;

/**
 * Adapts the AIPlayer class for use with the provider's interfaces.
 */
public class AIPlayerAdapter extends Player implements IPlayer, TurnAIPopUp {

  /**
   * Represents a computer player in the reversi game.
   */
  private final IStrategyAdapter strategy;
  private ReadonlyReversiModel board;

  /**
   * Constructor for an AIPlayer.
   */
  public AIPlayerAdapter(String name, PlayerType type,
                         ReadOnlyBoardModel board,
                         IStrategyAdapter strategy) {
    super("Computer", type, board);
    this.strategy = strategy;
    this.hasPassed = false;
  }

  /**
   * Makes a move for the AIPlayer.
   */
  public void makeMove() {
    Optional<HexShape> selectedMove = strategy.selectMove(this.board, board.curPlayer());
    if (selectedMove.isPresent()) {
      int row = parseInt(selectedMove.get().getRow());
      int column = parseInt(selectedMove.get().getColumn());
      super.makeMove(row, column);
    } else {
      this.setHasPassed();
    }
  }

  /**
   * Tells the AI, to make the move.
   */
  @Override
  public void itIsNowYourTurnMessage() {
    this.makeMove();
  }
}

