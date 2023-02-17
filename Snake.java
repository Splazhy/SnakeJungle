import java.util.ArrayList;
import java.awt.image.*;

/**
 * Snake in this game moves progressively, not cell-by-cell.
 */
public abstract class Snake {
  protected int x;
  protected int y;
  protected int curSpeed;
  protected int normalSpeed;
  protected int sprintSpeed;
  protected ArrayList<SnakePart> hitbox;
  protected GridMap gridMap;

  protected BufferedImage[] headSprite;
  protected BufferedImage[] bodySprite;
  protected BufferedImage[] tailSprite;
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

  public Snake(GridMap gridMap) {
    this.gridMap = gridMap;
    normalSpeed = 1;
    sprintSpeed = 3;
    curSpeed = normalSpeed;
  }

  /**
   * HEAD, BODY, TAIL
   */
  static class SnakePart {
    // TO-DO
  }
}
