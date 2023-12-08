import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import adapter.GameAdapter;
import adapter.ModelImpl;
import adapter.ReversiControllerAdapter;
import controller.Command;
import controller.Player;
import controller.ReversiController;
import model.Board;
import provider.model.ReversiModel;
import view.DrawUtils;
import provider.view.ReversiFrame;
import view.FrameSetup;

/**
 * Represents the GUI view.
 */
public class Reversi {

  private JLabel scoreLabel1; // Score label for the first frame
  private JLabel scoreLabel2; // Score label for the second frame

  /**
   * Entry point for GUI.
   */
  public static void main(String[] args) {

    Command commandLine = new Command(args);

    Board board = new Board();
    ReversiModel adapter = new ModelImpl();
    DrawUtils view1 = new DrawUtils(board);
    ReversiFrame view2 = new ReversiFrame(adapter);

    Player player1 = commandLine.getPlayer1();
    Player player2 = commandLine.getPlayer2();

    String score = "Black: " + board.getScoreBlack() + " White: " + board.getScoreWhite();

    JFrame frame1 = new JFrame("Reversi - Player 1");
    ReversiController controller1 = new ReversiController(player1, board, view1);
    player1.setMoveHandler(controller1);
    JLabel frame1Setup = FrameSetup.setupFrame(frame1, view1,
            "You are Player " + player1.getColor(), score);
    view1.setEventListener(controller1);
    view1.setScoreLabel(frame1Setup);

    view2.setVisible(true);

    ReversiControllerAdapter controller2 = new ReversiControllerAdapter(player2, adapter, view2);
    player1.setMoveHandler(controller1);

    adapter.startGame();
    view2.setHotKey(KeyStroke.getKeyStroke("typed p"), "passMove");
    view2.setHotKey(KeyStroke.getKeyStroke("typed t"), "placeTile");
    view2.addFeatures(controller2);

    GameAdapter game = new GameAdapter(controller2, controller1, board);
    game.start();
  }
}

