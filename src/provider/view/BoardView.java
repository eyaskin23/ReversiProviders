package provider.view;


import javax.swing.KeyStroke;

import provider.features.Features;

/**
 * Represents the frame view for a game of reversi and shows the board associated with this frame's
 * model. Provides functionality for a  client to use key presses to interact with the model,
 * where a key press of "t" allows the user to place a tile on the board for a game of reversi,
 * and a key press of "p" allows the user to pass their turn.
 */
public interface BoardView {

  /**
   * Unhighlights the most recently clicked on cell.
   */
  public void unHighlight();
 
  /**
   * Determines if the view is able to respond to key presses.
   * @return  true iff the view is able to respond
   */
  public boolean canPass();

  /**
   * Displays the given message in a pop-up dialogue box.
   * @param s   The string to display.
   */
  public void errorMessage(String s);

  /**
   * Repaints the view of the board based on the current state of its model.
   */
  public void updateBoard();


  /**
   * Adds the given feature to this frame's KeyComponent.
   * @param feature  A feature associated with this frame.
   */
  public void addFeatures(Features feature);


  /**
   * Associates the given key press with the string value of an action for this frame's
   * KeyComponent.
   * @param key A KeyStroke associated with this frame's functionality
   * @param featureName   The string value of an action that a feature of this KeyComponent has
   *            the ability to execute.
   */
  public void setHotKey(KeyStroke key, String featureName);


  /**
   * Displays the given string on this frame.
   * @param s   The string to display on the frame.
   */
  public void displayOutput(String s);


  /**
   * Returns the q coordinate of the most recently highlighted hexagonal cell
   * in this frame's panel.
   * @return  the q-coordinate value of the most recently highlighted hexagonal cell.
   */
  public int currentQCoordinate();


  /**
   * Returns the r coordinate of the most recently highlighted hexagonal cell in
   * this frame's panel.
   * @return  the r-coordinate value of the most recently highlighted hexagonal cell.
   */
  public int currentRCoordinate();

  /**
   * Updates this frame's panel to accept or not accept clicks based on the given
   * boolean value.
   * @param click   a boolean value for whether this frame's panel should
   *                currently accept clicks or not.
   */
  public void canClick(boolean click);

  /**
   * Displays this frame iff the given boolean is true.
   * @param show   A boolean, where if true, will display this frame.
   */
  public void display(boolean show);

  /**
   * Determines if a cell has been highlighted on this frame's panel.
   * @return  true iff this frame has a highlighted cell on its panel.
   */
  public boolean hasHighlightedCell();
}