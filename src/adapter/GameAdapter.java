package adapter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import controller.ReversiController;
import model.Board;
import view.GameMocked;

/**
 * Adapts the game class to start the game using my own interface.
 */
public class GameAdapter implements GameMocked {
  Board board;
  private Timer timer;
  private final ReversiControllerAdapter controller1;
  private final ReversiController controller2;

  /**
   * Constructor used for the GameAdapter.
   * Takes in the adapter controller, my controller,
   * and then my board.
   */
  public GameAdapter(ReversiControllerAdapter controller1,
                     ReversiController controller2, Board board) {
    this.board = new Board();
    this.controller1 = controller1;
    this.controller2 = controller2;
  }

  /**
   * Starts the game.
   */
  @Override
  public void start() {
    int delay = 1000;
    ActionListener listener = new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        if (!board.isGameOver()) {
          controller1.update();
          controller2.update();
        } else {
          stop();
        }
      }
    };
    timer = new Timer(delay, listener);
    timer.start();
  }

  /**
   * Quits the game.
   */
  public void stop() {
    if (timer != null) {
      timer.stop();
    }
  }
}
