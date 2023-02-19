import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Snake in this game moves progressively, not cell-by-cell.
 * @see PlayerSnake
 */
public abstract class Snake {
  /**
   * Head position (top-left point)
   */
  protected int headX, headY;
  protected double curSpeed;
  protected double normalSpeed;
  protected ArrayList<SnakePart> snakeList; // TO-DO calculate pos in tick()
  protected int[][] cellPos; // [{x,y} of frame, {x,y} of grid]
  protected int facing;
  private static int spriteSize;

  protected BufferedImage[] headSprite;
  protected BufferedImage[] bodySprite;
  protected BufferedImage[] tailSprite;
  /**
   * 0 for UP <p>
   * 1 for LEFT <p>
   * 2 for DOWN <p>
   * 3 for RIGHT <p>
   */

  public Snake() {
    normalSpeed = 2.4;
    curSpeed = normalSpeed;
    cellPos = new int[2][2];
    spriteSize = GridMap.size/GridMap.CELL_PER_ROW;
    facing = 3;
    headSprite = new BufferedImage[4];
    bodySprite = new BufferedImage[6];
    tailSprite = new BufferedImage[4];
    loadSprite();
    snakeList = new ArrayList<>();
    snakeList.add(new SnakeHead(facing, headSprite));
    snakeList.add(new SnakeBody(facing, snakeList.get(0), bodySprite));
    snakeList.add(new SnakeTail(facing, snakeList.get(1), tailSprite));
  }
  protected abstract void loadSprite();
  protected abstract void tick();

  /**
   * when resizing JFrame
   */
  protected void calibratePosition() {
    headX = GridMap.cellLayout[cellPos[1][1]][cellPos[1][0]][0];
    headY = GridMap.cellLayout[cellPos[1][1]][cellPos[1][0]][1];
    spriteSize = GridMap.size/GridMap.CELL_PER_ROW;
  }

  protected void draw(Graphics2D g2d) {
    for(int i = 0; i < snakeList.size(); i++) {
      snakeList.get(i).draw(g2d);
    }
  }

  private int getFacing() {
    return facing;
  }
  /**
   * HEAD, BODY, TAIL
   */
  private abstract class SnakePart {
    protected int facing;
    protected int pos;
    protected SnakePart followee;

    private SnakePart(int facing, SnakePart followee) {
      this.facing = facing;
      this.followee = followee;
    }
    protected abstract void draw(Graphics2D g2d);
  }

  private class SnakeHead extends SnakePart {
    private BufferedImage[] sprite;

    private SnakeHead(int facing, BufferedImage[] spriteArr) {
      super(facing, null);
      sprite = spriteArr;
    }
    protected void draw(Graphics2D g2d) {
      facing = getFacing();
      g2d.setClip(GridMap.offset[0], GridMap.offset[1], GridMap.size, GridMap.size);
      g2d.drawImage(sprite[facing], headX, headY, spriteSize, spriteSize, null);

      g2d.drawImage(sprite[facing], headX-GridMap.size, headY
      ,spriteSize, spriteSize, null);

      g2d.drawImage(sprite[facing], headX+GridMap.size, headY
      ,spriteSize, spriteSize, null);

      g2d.drawImage(sprite[facing], headX, headY-GridMap.size
      ,spriteSize, spriteSize, null);

      g2d.drawImage(sprite[facing], headX, headY+GridMap.size
      ,spriteSize, spriteSize, null);

      g2d.setClip(null);
    }
  }

  private class SnakeBody extends SnakePart {
    private BufferedImage[] sprite;

    private SnakeBody(int facing, SnakePart followee, BufferedImage[] spriteArr) {
      super(facing, followee);
      sprite = spriteArr;
    }
    @Override
    protected void draw(Graphics2D g2d) {

      g2d.setClip(GridMap.offset[0], GridMap.offset[1], GridMap.size, GridMap.size);
      
      // help

      g2d.setClip(null);
    }
  }

  private class SnakeTail extends SnakePart {
    private BufferedImage[] sprite;

    private SnakeTail(int facing, SnakePart followee, BufferedImage[] spriteArr) {
      super(facing, followee);
      sprite = spriteArr;
    }
    @Override
    protected void draw(Graphics2D g2d) {
      
      g2d.setClip(GridMap.offset[0], GridMap.offset[1], GridMap.size, GridMap.size);

      // help

      g2d.setClip(null);
    }
  }
}
