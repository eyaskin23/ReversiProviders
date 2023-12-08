package provider.model;

/**
 * Represents the coordinates for the hex board using the Axial coordinate system.
 * Where q is the q-coordinate, and r is the r-coordinate.
 */
public class HexCoordinates {
  // use ints for coordinates because they are specific whole number coordinates
  private final int q;
  private final int r;

  /**
   * the constructor for hexagon coordinates.
   * @param q the q component of the axial coordinate
   * @param r the r component of the axial coordinate
   */
  public HexCoordinates(int q, int r) {
    this.q = q;
    this.r = r;
  }

  /**
   * Determines if the given object is the same hex coordinate
   * as this hex coordinate.
   * @param obj   An object to compare with this hex coordinate.
   * @return    true iff the given object is a hex coordinate with
   *            the same q component and r component.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    HexCoordinates other = (HexCoordinates) obj;
    return q == other.q && r == other.r;
  }

  /**
   * Generates a hashcode for this hex coordinate.
   * @return The hashcode for this hex coordinate.
   */
  @Override
  public int hashCode() {
    int result;
    result = 13 * q;
    result += 21 * r;
    return result;
  }

  /**
   * return the q coordinate to the user.
   * @return the q coordinate of the axial coordinates
   */
  public int getQ() {
    return this.q;
  }

  /**
   * return the r coordinate to the user.
   * @return the r coordinate of the axial coordinates
   */
  public int getR() {
    return this.r;
  }
}