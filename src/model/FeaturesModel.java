package model;

/**
 * Represents any possible notifications the model can produce during the game
 * of reversi, where the model is able to notify the player when it's their turn,
 * when the game is over, and when they have passed.
 */
public interface FeaturesModel {


  /**
   * Determines if this features handler's player color is the current player for
   * this feature handler's model. If it is, displays that it is this feature handler's
   * view's turn and allows the player to click. Then it checks to see if the player
   * has any valid moves. If it is not the given player's turn and the game is not
   * over, displays that it is not the players turn and does not allow the player
   * to click on their view.
   */
  public void checkTurn();


  /**
   * Checks if this features handler's model's game is over. If it is, displays
   * the appropriate output to the view with the correct winner.
   */
  public void checkGameOver();

}
