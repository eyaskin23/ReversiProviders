package provider.view;

import provider.model.HexCoordinates;
import provider.model.ReadonlyReversiModel;
import controller.PlayerColor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

/**
 * Represents and draws a panel for a reversi game, where a panel includes the board and any of
 * its tiles, and the board is arranged in a hexagonal pattern with individual cells of the board
 * being hexagons. Allows a client to click on individual cells of the board and highlights the
 * clicked on cell. A user can deselect a cell by clicking on a different cell, clicking on the same
 * cell again, or by clicking off of the board.
 */
public class ReversiPanel extends JPanel {
  // this size of the game board for this panel
  private final int size;
  // the model associated with this panel
  private final ReadonlyReversiModel model;

  // the list of all hexagons used in this panel's game board
  private final List<Path2D.Double> cartCoords;
  // the list of all hexagonal axial coordinates used in this panel's game board
  private final List<HexCoordinates> hexCoords;


  // the most currently clicked on q coordinate
  private int cellQ;
  // the most currently clicked on r coordinate
  private int cellR;

  // determines if the most currently clicked cell should be highlighted
  private boolean fill;
  // counts the number of times a cell has been clicked on in a row
  private int count;
  // determines if this panel should be accepting clicks
  private boolean canClick;

  /**
   * The constructor for this ReversiPanel.
   */
  public ReversiPanel(ReadonlyReversiModel model) {
    this.model = model;
    // initializes size to the size of the given models board
    this.size = model.getSizeOfBoard();
    // set this to an arbitrary q,r coordinate
    this.cellQ = 0;
    this.cellR = 0;
    // sets fill to false so no hexes are filled when board is initially drawn
    this.fill = false;
    this.count = 0;
    // allows users to click initially, turns will be determined and enforced by the controller
    this.canClick = true;
    // initially empty list of coordinates as no cells have been defined yet
    this.cartCoords = new ArrayList<>();
    this.hexCoords = new ArrayList<>();

    // the method below will set the preferred panel size
    // Math.sqrt(3) * 40 will give us the horizontal length of the hexagon
    // 80 is the vertical length of the hexagon, but since the hexagons are
    //  coupled into each other, their combined vertical length is 60 * size +
    this.setPreferredSize(new Dimension((int) (Math.sqrt(3) * 40 * size + 20),
            (60 * size + 80)));

    // the listeners for mouse events
    MouseEventsListener listener = new MouseEventsListener();
    this.addMouseListener(listener);
    setFocusable(true);
    requestFocusInWindow();
  }


  /**
   * Unhighlights the most recently highlighted hexagon.
   */
  public void unHighlight() {
    this.fill = false;
  }

  /**
   * Determines if this panel can respond to clicks.
   * @return   true iff this panel can respond to clicks.
   */
  boolean getCanClick() {
    return this.canClick;
  }

  /**
   * Draws all hexagonal cells for the board associated with this panel. The hexagonal
   * cells are arranged in a hexagonal pattern, and placed on the panel on the pixel
   * coordinate system (0,0 is the upper left corner). Highlights the most recently
   * clicked cell in the board, and draws any tiles at the correct tiles on the board.
   *
   * @param g the Graphics object used to draw components on the panel.
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2D = (Graphics2D) g;
    int row = Math.floorDiv(size, 2);
    int colB = 0;
    int colU = row;
    int yPos = 1;
    for (int r = -row; r <= row; r++) {
      int count = 0;
      int numSpaces = Math.abs(colU + colB);
      // determines the pixel y-coordinate for a hexagon cell
      int y = (60 * yPos);
      yPos += 1;
      for (int q = colB; q <= colU; q++) {
        // determines the pixel x-coordinate for a hexagon cell
        int x = (int) ((numSpaces * Math.sqrt(3) * 20) + (count * Math.sqrt(3) * 40) + 40);
        count += 1;
        Path2D.Double hexagon = createHexagon(x, y);
        // adds the hexagon to this panel's list of hexagons
        this.cartCoords.add(hexagon);
        // adds the q,r coordinates of the most recently made hexagon to this panel's list
        // of hexagonal axial coordinates
        this.hexCoords.add(new HexCoordinates(q, r));
        boolean filled = (q == this.cellQ) && (r == this.cellR) && this.fill;
        drawHexagon(g2D, hexagon, filled);
        addTile(g2D, q, r, x, y);
      }
      if (colB == -row) {
        colU = colU - 1;
      } else if (colB <= 0 && colB >= -row) {
        colB = colB - 1;
      }
    }
  }


  /**
   * Adds the tile at the hexagon cell at the given q,r coordinate in this panel's reversi board.
   * The tile is white if the Provider.player.PlayerColor at the given coordinate is WHITE,
   * the tile is black if
   * the Provider.player.PlayerColor at the given coordinate is BLACK, and no tile is added
   * otherwise.
   * @param g2D the Graphics object used to draw components on the panel.
   * @param q   the q coordinate of the hexagonal cell on which a tile should be added.
   * @param r   the r coordinate of the hexagonal cell on which a tile should be added.
   * @param x   the pixel coordinate of the hexagonal cell to which a tile is being added.
   * @param y   the pixel coordinate of the hexagonal cell to which a tile is being added.
   */
  private void addTile(Graphics2D g2D, int q, int r, int x, int y) {
    PlayerColor color = model.getContentsOfCell(q, r);
    if (color == PlayerColor.BLACK) {
      g2D.setColor(Color.BLACK);
      g2D.fillOval(x - 15, y - 15, 30, 30);
    } else if (color == PlayerColor.WHITE) {
      g2D.setColor(Color.WHITE);
      g2D.fillOval(x - 15, y - 15, 30, 30);
    }

  }

  /**
   * Creates the outline of a hexagon that makeup the cells for this panel's reversi game board.
   *
   * @param x the x coordinate of the starting point on the panel.
   * @param y the y coordinate of the starting point on the panel.
   * @return the path for drawing a hexagon.
   */
  private Path2D.Double createHexagon(int x, int y) {
    Path2D.Double hexagon = new Path2D.Double();
    double sideLength = 40;
    // use a for loop to draw the lines of the hexagon
    for (int i = 0; i < 6; i++) {
      double angle = 2.0 * Math.PI * (i + 0.5) / 6;
      double xOffset = sideLength * Math.cos(angle);
      double yOffset = sideLength * Math.sin(angle);
      if (i == 0) {
        hexagon.moveTo(x + xOffset, y + yOffset);
      } else {
        hexagon.lineTo(x + xOffset, y + yOffset);
      }
    }
    // This method will draw a line from the last point to the starting point
    hexagon.closePath();
    return hexagon;
  }

  /**
   * Sets this panel's ability to accept clicks to the given boolean value.
   *
   * @param click The boolean value that determines whether this panel should
   *              accept clicks.
   */
  void setCanClick(boolean click) {
    this.canClick = click;
  }

  /**
   * Draw a hexagon using the Graphics2D class, where a hexagon is drawn in gray if it
   * is not currently selected, and the hexagon is drawn in cyan if it has been
   * selected.
   *
   * @param g       the Graphics2D class that contains methods to draw hexagons.
   * @param hexagon The hexagon that is being drawn.
   */
  private void drawHexagon(Graphics2D g, Path2D.Double hexagon, boolean clicked) {
    g.setColor(Color.BLACK);
    g.draw(hexagon);
    if (clicked) {
      g.setColor(Color.CYAN);
      g.fill(hexagon);
    } else {
      g.setColor(Color.GRAY);
      g.fill(hexagon);
    }
  }

  /**
   * Returns this panel's most recently selected axial q position.
   *
   * @return The q coordinate of the most recently selected cell of the board of this panel.
   */
  int getCellQ() {
    return this.cellQ;
  }


  /**
   * Returns this panel's most recently selected axial r position.
   *
   * @return The r coordinate of the most recently selected cell of the board of this panel.
   */
  int getCellR() {
    return this.cellR;
  }


  /**
   * Represents any functionality associated with user mouse inputs.
   */
  private class MouseEventsListener extends MouseInputAdapter {
    /**
     * Responds to a mouse click, if this panel is currently accepting clicks,
     * by determining the axial coordinate of the click. If the axial coordinate
     * is a cell on this panel's board, highlights the cell or deselects the most recently
     * highlighted cell if a cell was already highlighted during the click.
     *
     * @param e The event to be processed.
     */
    @Override
    public void mousePressed(MouseEvent e) {
      if (ReversiPanel.this.canClick) {
        int x = e.getX();
        int y = e.getY();
        int q = convertToHex(x, y).getQ();
        int r = convertToHex(x, y).getR();

        // set the highlighted cell coordinates
        this.checkFill(q, r);

        // Print the logical coordinates to the console
        ReversiPanel.this.repaint();
        System.out.println("you clicked on: " + q + ", " + r);
      }
    }

    /**
     * Determines if the cell on this panel's board at the given q,r coordinate is currently
     * highlighted, if it is, deselects that cell, if it has not been highlighted, sets
     * the most recent q,r coordinate for this panel to the given q,r coordinate and updates
     * this panel's fill value accordingly. If the given q,r coordinate is not on the board,
     * deselects the cell that is currently highlighted, if there is a cell currently highlighted.
     *
     * @param q The q-axial coordinate on this panel.
     * @param r THE r-axial coordinate on this panel.
     */
    private void checkFill(int q, int r) {
      if (ReversiPanel.this.cellQ == q && ReversiPanel.this.cellR == r
              && ReversiPanel.this.count == 1) {
        ReversiPanel.this.fill = false;
        ReversiPanel.this.count = 0;
      } else if (!ReversiPanel.this.model.isValidCoordinate(q, r)) {
        ReversiPanel.this.fill = false;
      } else {
        ReversiPanel.this.cellQ = q;
        ReversiPanel.this.cellR = r;
        ReversiPanel.this.fill = true;
        ReversiPanel.this.count = 1;
      }
    }
  }


  /**
   * Converts the given pixel x,y coordinates (0,0 in the upper left corner) to the
   * axial coordinate system by determining what hexagonal cell the x,y coordinate is
   * contained in. If the click is off of the hex board, returns an arbitrary axial coordinate
   * that is off of the board.
   *
   * @param x The x-coordinate of a click on the panel in the pixel coordinate system.
   * @param y The y-coordinate of a click on the panel in the pixel coordinate system.
   * @return The hex coordinate at the given x,y on the panel where the hex coordinate
   *         is given in the axial q,r coordinate system.
   */
  private HexCoordinates convertToHex(int x, int y) {
    for (int i = 0; i < this.cartCoords.size(); i++) {
      if (cartCoords.get(i).contains(x, y)) {
        return this.hexCoords.get(i);
      }
    }
    return new HexCoordinates(this.size + 1, this.size + 1);
  }

  /**
   * Determines if any cell is highlighted on this panel's board.
   *
   * @return true iff a cell is highlighted on this panel's board.
   */
  boolean checkHighlight() {
    return this.fill;
  }
}

