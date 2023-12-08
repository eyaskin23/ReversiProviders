package adapter;

import controller.Player;
import controller.PlayerType;
import model.Board;
import model.ReadOnlyBoardModel;

/**
 * Adapter class for using the new command line.
 */
public class CommandAdapter {
  private Player player1;
  private Player player2;
  private int boardSize;
  private ReadOnlyBoardModel board;

  /**
   * Initializes the command line arguments.
   */
  public CommandAdapter(String[] args) {
    parseArguments(args);
  }

  /**
   * Adapts the command line class for use with provided strategies.
   */
  private void parseArguments(String[] args) {
    if (args.length < 2) {
      throw new IllegalArgumentException(
              "Insufficient arguments. Expected at least" +
                      " board size and player 1 type or strategy.");
    }

    this.boardSize = Integer.parseInt(args[0]);
    this.board = new Board(boardSize);

    player1 = findThePlayer(args, 1);
    player2 = findThePlayer(args, 2);
  }

  /**
   * Determines, the player made.
   */
  private Player findThePlayer(String[] args, int playerNumber) {
    String playerType = "human";
    String strategy = null;
    int index = -1;

    if (playerNumber == 1) {
      index = 1;
      if (args.length > 3 && isStrategy(args[3])) {
        strategy = args[3];
      }
    } else if (playerNumber == 2 && args.length > 2) {
      index = 2;
    }

    if (index != -1) {
      if (isStrategy(args[index])) {
        playerType = "ai";
        if (strategy == null) {
          strategy = args[index];
        }
      } else {
        playerType = args[index];
      }
    }

    PlayerType pType = PlayerType.BLACK;
    if (playerNumber == 2) {
      pType = PlayerType.WHITE;
    }

    return createPlayer(pType, playerType, strategy);
  }


  /**
   * Checks to see if a strategy was correctly, made.
   */
  private boolean isStrategy(String input) {
    switch (input.toLowerCase()) {
      case "capture":
      case "corner":
      case "capture corner":
      case "corner capture":
      case "capture capture":
      case "corner corner":
        return true;
      default:
        return false;
    }
  }

  /**
   * Creates a player.
   */
  private Player createPlayer(PlayerType playerType, String playerTypeStr, String strategyStr) {
    if (playerTypeStr.equalsIgnoreCase("ai")) {
      IStrategyAdapter strategy = getStrategy(strategyStr);
      return new AIPlayerAdapter("AI Player", playerType, board, strategy);
    } else {
      return new Player("Human Player", playerType, board);
    }
  }

  /**
   * Makes the Provider.player.Strategy based on the string passed.
   */
  private IStrategyAdapter getStrategy(String strategyStr) {
    switch (strategyStr.toLowerCase()) {
      case "capture":
        return new CapturePieceAdapter();
      case "capture capture":
        return new CapturePieceAdapter();
      case "corner":
        return new CheckCornersAdapter();
      default:
        throw new IllegalArgumentException("Invalid strategy: " + strategyStr);
    }
  }

  /**
   * Returns the boardSize that is inputted.
   */
  public int getBoardSize() {
    return boardSize;
  }

  /**
   * Returns the first player.
   */
  public Player getPlayer1() {
    return player1;
  }

  /**
   * Returns the second player.
   */
  public Player getPlayer2() {
    return player2;
  }

  /**
   * Gets the regular board, from the readOnly Board that the players use.
   */
  public Board getBoard() {
    return board.getRegularBoard();
  }
}
