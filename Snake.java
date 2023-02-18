import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Snake in this game moves progressively, not cell-by-cell.
 * @see PlayerSnake
 */
public abstract class Snake {
  protected int x, y;
  protected double curSpeed;
  protected double normalSpeed;
  protected ArrayList<Rectangle> hitbox;
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

  public Snake() {
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

  protected void draw(Graphics2D g2d) {
    drawHead(g2d);
  }

  /**
  * draws in all adjacent directions
  * @param g2d
  */
  private void drawHead(Graphics2D g2d) {
    int spriteSize = GridMap.size/GridMap.numOfGrid;
    g2d.setClip(GridMap.offset[0], GridMap.offset[1], GridMap.size, GridMap.size);
    g2d.drawImage(headSprite[facing], x, y, spriteSize, spriteSize, null);

    g2d.drawImage(headSprite[facing], x-GridMap.size, y
    ,spriteSize, spriteSize, null);

    g2d.drawImage(headSprite[facing], x+GridMap.size, y
    ,spriteSize, spriteSize, null);

    g2d.drawImage(headSprite[facing], x, y-GridMap.size
    ,spriteSize, spriteSize, null);

    g2d.drawImage(headSprite[facing], x, y+GridMap.size
    ,spriteSize, spriteSize, null);

    g2d.setClip(null);
  }
  /**
   * HEAD, BODY, TAIL
   */
  static class SnakePart {
    // TO-DO
  }
}
