import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 * Snake in this game moves progressively, not cell-by-cell.
 * @see PlayerSnake
 */
public abstract class Snake {
  protected int x;
  protected int y;
  protected double curSpeed;
  protected double normalSpeed;
  protected ArrayList<SnakePart> hitbox;
  protected int[] cellPos;

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

  public Snake(GridMap gridMap) {
    normalSpeed = 2.4;
    curSpeed = normalSpeed;
    cellPos = new int[2];
  }

  protected void getCellPos(int x, int y) {
    cellPos = GridMap.getCellPos(x, y);
  }

  /**
   * HEAD, BODY, TAIL
   */
  static class SnakePart {
    // TO-DO
  }
}
