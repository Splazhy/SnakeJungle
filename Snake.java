/**
 * Snake in this game moves progressively, not cell-by-cell.
 */

public abstract class Snake {
  protected int x;
  protected int y;
  protected int speed;
  /**
   * 0 for UP <p>
   * 1 for LEFT <p>
   * 2 for DOWN <p>
   * 3 for RIGHT <p>
   */
  protected int facing;
  /**
   * Represents size of the snake
   * excluding head and tail.
   */
  protected int size;

  public Snake() {
    x = 320;
    y = 320;
    speed = 3;
    facing = 3;
    size = 1;
  }
}
