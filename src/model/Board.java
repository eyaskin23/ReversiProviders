package model;

import controller.DirectionsEnum;
import controller.Player;
import controller.PlayerType;
import view.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Sets up a board for the controller to use.
 */
public class Board implements ReadOnlyBoardModel, BoardModel {
  private boolean isGameOver = false;
  private List<Observer> observers = new ArrayList<>();

  @Override
  public void addObserver(Observer o) {
    observers.add(o);
  }

  /**
   * Notifies the observers of any changes, the Observer pattern.
   */
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.update();
    }
  }

  private PlayerType currentTurn;
  public int boardSize;
  public HexShape[][] cellsThatMakeTheBoard;
  private boolean whitePassed = false;
  private boolean blackPassed = false;

  /**
   * Controller used in the mine to set the size of the board.
   * A default board is size 7.
   */
  public Board() {
    this(7);
  }

  /**
   * Constructor used for testing.
   * Throws an exception if the game size is even or less than 5,
   * because a valid hexagon cannot be created from either value.
   */
  public Board(int sizeOfBoard) {
    this.currentTurn = PlayerType.BLACK;
    if (sizeOfBoard < 5 || (sizeOfBoard % 2 == 0)) {
      throw new IllegalStateException("The game must be a minimum of size 5 and cannot be even!");
    }

    boardSize = sizeOfBoard;
    cellsThatMakeTheBoard = new HexShape[boardSize][boardSize];
    int midPoint = boardSize / 2;

    for (int row = 0; row < boardSize; row++) {
      int startQ;
      int endQ;

      if (row <= midPoint) {
        startQ = midPoint - row;
        endQ = boardSize;
      } else {
        startQ = 0;
        endQ = boardSize - row + midPoint;
      }

      for (int column = startQ; column < endQ; column++) {
        int q = column - midPoint;
        int r = row - midPoint;
        cellsThatMakeTheBoard[row][column] = new HexShape(r, q, null);
      }
    }
    this.getCurrentHex(this.boardSize / 2,
            this.boardSize / 2 + 1).setPlayerType(PlayerType.BLACK);
    this.getCurrentHex(this.boardSize / 2 + 1,
            this.boardSize / 2).setPlayerType(PlayerType.WHITE);
    this.getCurrentHex(this.boardSize / 2,
            this.boardSize / 2 - 1).setPlayerType(PlayerType.WHITE);
    this.getCurrentHex(this.boardSize / 2 + 1,
            this.boardSize / 2 - 1).setPlayerType(PlayerType.BLACK);
    this.getCurrentHex(this.boardSize / 2 - 1,
            this.boardSize / 2).setPlayerType(PlayerType.BLACK);
    this.getCurrentHex(this.boardSize / 2 - 1,
            this.boardSize / 2 + 1).setPlayerType(PlayerType.WHITE);

  }

  /**
   * Returns the current hex shape based on row and column.
   */
  public HexShape getCurrentHex(int row, int column) {
    return cellsThatMakeTheBoard[row][column];
  }

  public ReadOnlyBoardModel getReadOnlyBoard() {
    return this;
  }


  public void switchTurns() {
    currentTurn = (currentTurn == PlayerType.BLACK) ? PlayerType.WHITE : PlayerType.BLACK;
    notifyObservers();
  }

  /**
   * Checks if it is the current player's turn.
   */
  public boolean isPlayerTurn(Player player) {
    if (player == null) {
      throw new IllegalStateException("Player is null");
    }
    if (player.getType() == null) {
      throw new IllegalStateException("PlayerType is null");
    }
    if (currentTurn == null) {
      throw new IllegalStateException("Current Turn is null");
    }
    return currentTurn == player.getType();
  }


  /**
   * Flips a player type based on passed in coordinates passed
   * in by the player.
   * Breaks out of the loop if the player, opponent,
   * or direction is invalid.
   */
  public void flipPieces(int x, int y, PlayerType currentPlayer) {
    if (currentPlayer == null) {
      System.out.println("Current player is null!");
      return;
    }

    PlayerType opponent = currentPlayer.nextPlayer();
    if (opponent == null) {
      System.out.println("Opponent is null!");
      return;
    }

    for (DirectionsEnum dir : DirectionsEnum.values()) {
      int nextQ = x + dir.getQMove();
      int nextR = y + dir.getRMove();

      // Skip if the next hex is not opponent's or out of bounds
      if (!isValidCoordinate(nextQ, nextR)) {
        continue;
      }
      HexShape neighborHex = getCurrentHex(nextR, nextQ);
      if (neighborHex == null) {
        continue;
      }

      if (neighborHex.getPlayerType() != opponent) {
        continue;
      }

      List<HexShape> piecesToFlip = new ArrayList<>();

      while (isValidCoordinate(nextQ, nextR) && getCurrentHex(nextR, nextQ) != null
              && getCurrentHex(nextR, nextQ).getPlayerType() == opponent) {
        piecesToFlip.add(getCurrentHex(nextR, nextQ));
        nextQ += dir.getQMove();
        nextR += dir.getRMove();

        if (isValidCoordinate(nextQ, nextR) && getCurrentHex(nextR, nextQ) != null
                && getCurrentHex(nextR, nextQ).getPlayerType() == currentPlayer) {
          for (HexShape piece : piecesToFlip) {
            piece.setPlayerType(currentPlayer);
          }
          break;
        }
      }
    }
  }


  /**
   * Determines if a valid move is passed in,
   * based on coordinates, and a player type.
   */
  public boolean isValidMove(int x, int y, PlayerType playerType) {
    int q = x + this.getBoardSize() / 2;
    int r = y + this.getBoardSize() / 2;

    // Check if the coordinates are valid and the hex is empty
    if (!isValidCoordinate(q, r) || getCurrentHex(r, q).getPlayerType() != PlayerType.EMPTY) {
      return false;
    }

    PlayerType opponent = playerType.nextPlayer();

    // Check each direction for a valid line of opponent's pieces
    for (DirectionsEnum dir : DirectionsEnum.values()) {
      int nextQ = q + dir.getQMove();
      int nextR = r + dir.getRMove();

      // Skip if the next hex is not opponent's or out of bounds
      if (!isValidCoordinate(nextQ, nextR)) {
        continue;
      }
      HexShape neighborHex = getCurrentHex(nextR, nextQ);
      if (neighborHex == null) {
        continue;
      }

      if (neighborHex.getPlayerType() != opponent) {
        continue;
      }

      // Move to the next hex in the same direction
      nextQ += dir.getQMove();
      nextR += dir.getRMove();

      // Continue moving in the direction and check for current player's piece
      while (isValidCoordinate(nextQ, nextR)
              && getCurrentHex(nextR, nextQ) != null
              && getCurrentHex(nextR, nextQ).getPlayerType() == opponent) {
        nextQ += dir.getQMove();
        nextR += dir.getRMove();
      }

      // If a current player's piece is found, it's a valid move
      if (isValidCoordinate(nextQ, nextR) && getCurrentHex(nextR, nextQ) != null
              && getCurrentHex(nextR, nextQ).getPlayerType() == playerType) {
        return true;
      }
    }
    return false;
  }


  /**
   * Determines is a valid coordinate was passed in
   * based on rows and columns.
   */
  public boolean isValidCoordinate(int q, int r) {
    return q >= 0 && q < this.getBoardSize() && r >= 0 && r < this.getBoardSize();
  }


  /**
   * Places a certain piece in the board, based on
   * row and column, and playerType.
   */
  public void placePiece(int q, int r, PlayerType type) {
    this.getCurrentHex(r, q).setPlayerType(type);
    whitePassed = false;
    blackPassed = false;
    checkGameOver();
  }

  /**
   * Changes the state of each player if they passed or not.
   */
  public void playerPass(PlayerType playerType) {
    if (playerType == PlayerType.WHITE) {
      whitePassed = true;
    } else if (playerType == PlayerType.BLACK) {
      blackPassed = true;
    }
  }

  @Override
  public int getScoreWhite() {
    if (isGameOver()) {
      return countPieces(PlayerType.WHITE);
    }
    return countPieces(PlayerType.WHITE);
  }

  @Override
  public int getScoreBlack() {
    if (isGameOver()) {
      return countPieces(PlayerType.BLACK);
    }
    return countPieces(PlayerType.BLACK);
  }


  /**
   * Returns the size of a board.
   */
  @Override
  public int getBoardSize() {
    return this.boardSize;
  }

  /**
   * Determines whether a game is over
   * based on if the board is full, or if both
   * players have skipped their turn.
   */
  public boolean isGameOver() {
    return (isBoardFull() || bothPlayersPassed());
  }

  /**
   * Determines if both players have skipped their turns.
   */
  private boolean bothPlayersPassed() {
    return whitePassed && blackPassed;
  }

  /**
   * Determines whether a player is trapped, and has no valid next move.
   */
  private boolean isPlayerTrapped(PlayerType player) {
    for (int i = 0; i < boardSize; i++) {
      for (int j = 0; j < boardSize; j++) {
        HexShape hex = cellsThatMakeTheBoard[i][j];
        if (hex != null && hex.getPlayerType() == player) {
          if (isValidMove(i - boardSize / 2, j - boardSize / 2, player)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Determines if a board is completely full,
   * causing the game to be over.
   */
  public boolean isBoardFull() {
    for (int i = 0; i < boardSize; i++) {
      for (int j = 0; j < boardSize; j++) {
        HexShape hex = getCurrentHex(i, j);
        if (hex == null) {
          continue;
        }
        if (hex.getPlayerType() == null || hex.getPlayerType() == PlayerType.EMPTY) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Counts the amount of pieces that exist in a board.
   */
  public int countPieces(PlayerType type) {
    int count = 0;
    for (int i = 0; i < boardSize; i++) {
      for (int j = 0; j < boardSize; j++) {
        if (getCurrentHex(i, j) != null && getCurrentHex(i, j).getPlayerType() == type) {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * Returns the midPoint of a board.
   */
  public int getMidPoint() {
    return this.getBoardSize() / 2;
  }

  @Override
  public Board getRegularBoard() {
    return this;
  }

  @Override
  public PlayerType getCurrentTurn() {
    return this.currentTurn;
  }

  /**
   * Checks whether a player has passed their turn.
   */
  public boolean hasPlayerPassed(PlayerType type) {
    if (type == PlayerType.WHITE) {
      return whitePassed;
    } else if (type == PlayerType.BLACK) {
      return blackPassed;
    }
    return false;
  }

  /**
   * Makes a deep copy of the board that players can access.
   */
  public Board deepCopy() {
    Board newBoard = new Board(this.boardSize);
    for (int i = 0; i < boardSize; i++) {
      for (int j = 0; j < boardSize; j++) {
        if (this.cellsThatMakeTheBoard[i][j] != null) {
          newBoard.cellsThatMakeTheBoard[i][j] = new HexShape(
                  i, j, this.cellsThatMakeTheBoard[i][j].getPlayerType());
        }
      }
    }
    newBoard.whitePassed = this.whitePassed;
    newBoard.blackPassed = this.blackPassed;
    return newBoard;
  }

  /**
   * Gets a valid list of moves that a player can play.
   */
  public List<Move> getValidMovesWithCaptures(Player player) {
    List<Move> validMoves = new ArrayList<>();

    for (int currentRow = 0; currentRow < getBoardSize(); currentRow++) {
      int currentHexesMade;
      if (currentRow <= getMidPoint()) {
        currentHexesMade = getMidPoint() + currentRow + 1;
      } else {
        currentHexesMade = getBoardSize() - (currentRow - getMidPoint());
      }
      int spacesBefore = (getBoardSize() - currentHexesMade);
      for (int h = 0; h < currentHexesMade; h++) {
        HexShape currentHex;

        if (currentRow <= getMidPoint()) {
          currentHex = this.getCurrentHex(currentRow, h + spacesBefore);

          if (isValidMove(Integer.parseInt(currentHex.getColumn()),
                  Integer.parseInt(currentHex.getRow()), player.getType())) {

            int piecesThatAreFlipped = calculateCaptures((Integer.parseInt(currentHex.getColumn())),
                    Integer.parseInt(currentHex.getRow()), player.getType(), this);

            validMoves.add(new Move((Integer.parseInt(currentHex.getColumn())),
                    Integer.parseInt(currentHex.getRow()), piecesThatAreFlipped));
          }

        } else {
          currentHex = this.getCurrentHex(currentRow, h);

          if (isValidMove(Integer.parseInt(currentHex.getColumn()),
                  Integer.parseInt(currentHex.getRow()), player.getType())) {

            int piecesThatAreFlipped = calculateCaptures((Integer.parseInt(currentHex.getColumn())),
                    Integer.parseInt(currentHex.getRow()), player.getType(), this);

            validMoves.add(new Move((Integer.parseInt(currentHex.getColumn())),
                    Integer.parseInt(currentHex.getRow()), piecesThatAreFlipped));
          }
        }
      }

    }
    return validMoves;
  }

  /**
   * Checks if a move is a corner move.
   */
  @Override
  public boolean isCornerMove(Move move, int boardSize) {
    int x = move.getX();
    int y = move.getY();
    return (x == 0 && y == 0) ||
            (x == 0 && y == boardSize - 1) ||
            (x == boardSize - 1 && y == 0) ||
            (x == boardSize - 1 && y == boardSize - 1);
  }


  /**
   * Calculates the amount of capture a move does.
   */
  public int calculateCaptures(int q, int r, PlayerType player, Board board) {
    int x = q + board.getBoardSize() / 2;
    int y = r + board.getBoardSize() / 2;
    int count = 0;

    PlayerType opponent = player.nextPlayer();

    for (DirectionsEnum dir : DirectionsEnum.values()) {

      int nextQ = x + dir.getQMove();
      int nextR = y + dir.getRMove();

      // Skip if the next hex is not opponent's or out of bounds
      if (!isValidCoordinate(nextQ, nextR)) {
        continue;
      }
      HexShape neighborHex = getCurrentHex(nextR, nextQ);
      if (neighborHex == null) {
        continue;
      }

      if (neighborHex.getPlayerType() != opponent) {
        continue;
      }
      List<HexShape> piecesToFlip = new ArrayList<>();

      while (isValidCoordinate(nextQ, nextR) && getCurrentHex(nextR, nextQ) != null
              && getCurrentHex(nextR, nextQ).getPlayerType() == opponent) {
        piecesToFlip.add(getCurrentHex(nextQ, nextR));
        nextQ += dir.getQMove();
        nextR += dir.getRMove();

        if (isValidCoordinate(nextQ, nextR) && getCurrentHex(nextR, nextQ) != null) {
          HexShape currentHex = getCurrentHex(nextR, nextQ);
          if (currentHex.getPlayerType() == player) {
            count += piecesToFlip.size();
            break;
          }
        }
      }
    }
    return count;
  }

  /**
   * Resets the white Passed boolean.
   */
  public void resetWhitePassed() {
    whitePassed = false;
  }

  /**
   * Resets the black Passed boolean.
   */
  public void resetBlackPassed() {
    blackPassed = false;
  }


  /**
   * Checks to see if the game is over to notify observers.
   */
  public void checkGameOver() {
    if (isGameOver()) {
      isGameOver = true;
      notifyObserversGameOver();
    }
  }

  /**
   * Notifies observers that the game is over.
   */
  public void notifyObserversGameOver() {
    for (Observer observer : observers) {
      observer.onGameOver();
    }
  }
}