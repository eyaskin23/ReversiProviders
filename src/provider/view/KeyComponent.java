package provider.view;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import provider.features.Features;


/**
 * Associates key events with specific client action commands, where "passMove" allows a client
 * to pass their move, and "placeTile" allows a client to place on a tile on a reversi game board.
 */
public class KeyComponent extends JPanel {
  private final List<Features> featureListeners = new ArrayList<>();

  /**
   * Adds the given feature to this KeyComponent list of features.
   *
   * @param f A feature to add to this KeyComponent.
   */
  public void addFeatures(Features f) {
    this.featureListeners.add(f);
  }

  /**
   * Constructor for KeyComponent. Initializes the action map for this
   * key component to have "passMove" and "placeTile" functionality,
   * where passMove allows a client to pass their turn and placeTile
   * allows the client to place a tile on the board.
   */
  public KeyComponent() {
    this.getActionMap().put("passMove", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        for (Features f : featureListeners) {
          f.passMove();
        }
      }
    });
    this.getActionMap().put("placeTile", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        for (Features f : featureListeners) {
          f.placeTile();
        }
      }
    });
  }


}