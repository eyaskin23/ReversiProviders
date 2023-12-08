package provider.view;

import provider.model.ReadonlyReversiModel;
import provider.features.Features;
import view.PlayerActionListener;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * Represents the frame view for a game of reversi and shows the board associated with this frame's
 * model. Provides functionality for a client to use key presses to interact with the model,
 * where a key press of "t" allows the user to place a tile on the board for a game of reversi,
 * and a key press of "p" allows the user to pass their turn.
 */
public class ReversiFrame extends JFrame implements BoardView {
  // the panel associated with the model for this frame
  private final ReversiPanel panel;
  // the set of action commands associated with any key presses in this frame
  private final KeyComponent keyComponent;
  private PlayerActionListener playerAction;
  private JLabel scoreLabel;

  /**
   * The constructor for this frame.
   * @param model The model for this frame.
   */
  public ReversiFrame(ReadonlyReversiModel model) {
    this.setLayout(new FlowLayout());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.panel = new ReversiPanel(model);
    this.add(panel);
    this.pack();

    this.keyComponent = new KeyComponent();
    this.add(keyComponent);
    this.keyComponent.requestFocus();
  }

  /**
   * Unhighlight the most recently highlighted cell.
   */
  public void unHighlight() {
    this.panel.unHighlight();
  }

  /**
   * Determines if the view is able to respond to key presses.
   * @return  true iff the view is able to respond
   */
  public boolean canPass() {
    return this.panel.getCanClick();
  }

  /**
   * Displays the given message in a pop-up dialogue box.
   * @param s   The string to display.
   */
  public void errorMessage(String s) {
    JOptionPane.showConfirmDialog(this, s,
            "Invalid Move!", JOptionPane.OK_CANCEL_OPTION);

  }

  /**
   * Repaints the view of the board based on the current state of its model.
   */
  public void updateBoard() {
    this.repaint();
  }

  /**
   * Adds the given feature to this frame's KeyComponent.
   * @param feature  A feature associated with this frame.
   */
  public void addFeatures(Features feature) {
    this.keyComponent.addFeatures(feature);
  }

  /**
   * Associates the given key press with the string value of an action for this frame's
   * KeyComponent.
   * @param key A KeyStroke associated with this frame's functionality
   * @param featureName   The string value of an action that a feature of this KeyComponent has
   *            the ability to execute.
   */
  public void setHotKey(KeyStroke key, String featureName) {
    this.keyComponent.getInputMap().put(key, featureName);
  }

  /**
   * Displays the given string on this frame's panel.
   * @param s   The string to display on the panel.
   */
  public void displayOutput(String s) {
    this.setTitle(s);
  }

  /**
   * Returns the q coordinate of the most recently highlighted hexagonal cell
   * in this frame's panel.
   * @return  the q-coordinate value of the most recently highlighted hexagonal cell.
   */
  public int currentQCoordinate() {
    return this.panel.getCellQ();
  }


  /**
   * Returns the r coordinate of the most recently highlighted hexagonal cell in
   * this frame's panel.
   * @return  the r-coordinate value of the most recently highlighted hexagonal cell.
   */
  public int currentRCoordinate() {
    return this.panel.getCellR();
  }

  /**
   * Updates this frame's panel to accept or not accept clicks based on the given
   * boolean value.
   * @param click   a boolean value for whether this frame's panel should
   *                currently accept clicks or not.
   */
  public void canClick(boolean click) {
    this.panel.setCanClick(click);
  }


  /**
   * Displays this frame iff the given boolean is true.
   * @param show   A boolean, where if true, will display this frame.
   */
  public void display(boolean show) {
    this.setVisible(show);
  }


  /**
   * Determines if a cell has been highlighted on this frame's panel.
   * @return  true iff this frame has a highlighted cell on its panel.
   */
  public boolean hasHighlightedCell() {
    return this.panel.checkHighlight();
  }

  public void showInvalidMoveMessage() {
    JOptionPane.showMessageDialog(this, "Invalid move, please try again.",
            "Invalid Move", JOptionPane.ERROR_MESSAGE);
  }
}