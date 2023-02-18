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
  protected int[][] cellPos; // [{x,y} of frame][{x,y} of grid]

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
    cellPos = new int[2][2];
  }

  /**
   * when resizing JFrame
   */
  protected void calibratePosition() {
    x = GridMap.cellLayout[cellPos[1][1]][cellPos[1][0]][0];
    y = GridMap.cellLayout[cellPos[1][1]][cellPos[1][0]][1];
  }

  /**
   * HEAD, BODY, TAIL
   */
  static class SnakePart {
    // TO-DO
  }
}
