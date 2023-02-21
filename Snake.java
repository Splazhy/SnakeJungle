import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
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
  protected int[][] cellPos; // [{x,y} of frame, {x,y} of grid] of head
  /**
   * 0 for UP <p>
   * 1 for LEFT <p>
   * 2 for DOWN <p>
   * 3 for RIGHT <p>
   */
  protected int facing;

  protected BufferedImage[] headSprite;
  protected BufferedImage[] bodySprite;
  protected BufferedImage[] tailSprite;

  public Snake() {
    normalSpeed = 2.4;
    curSpeed = normalSpeed;
    cellPos = new int[2][2];
    facing = 3;
    headSprite = new BufferedImage[4];
    bodySprite = new BufferedImage[6];
    tailSprite = new BufferedImage[4];
    loadSprite();
    snakeList = new ArrayList<>();
    snakeList.add(new SnakeHead(facing, headSprite));
    for(int i = 0; i < 100; i++)
      snakeList.add(new SnakeBody(facing, snakeList.get(i), bodySprite));
    snakeList.add(new SnakeTail(facing, snakeList.get(snakeList.size()-1), tailSprite));
  }

  protected abstract void loadSprite();

  protected void tick() {
    for(int i = 0; i < snakeList.size(); i++) {
      snakeList.get(i).tick();
    }
    cellPos = GridMap.getCellPos(headX, headY);
  }

  protected void draw(Graphics2D g2d) {
    for(int i = 0; i < snakeList.size(); i++) {
      snakeList.get(i).draw(g2d);
    }
  }
  
  /**
   * HEAD, BODY, TAIL
   */
  private abstract class SnakePart {
    protected int x, y;
    protected int subfacing;
    protected int moveData;
    protected SnakePart followee;
    protected Deque<Integer> moveQueue;

    private SnakePart(int facing, SnakePart followee) {
      x = headX; y = headY;
      subfacing = facing;
      this.followee = followee;
      moveQueue = new LinkedList<>();
    }

    protected abstract void tick();
    protected abstract void draw(Graphics2D g2d);

  }

  private class SnakeHead extends SnakePart {
    private BufferedImage[] sprite;

    private SnakeHead(int facing, BufferedImage[] spriteArr) {
      super(facing, null);
      sprite = spriteArr;
    }

    @Override
    protected void tick() {
      switch(facing) {
      case 0:
        headY = (headY > 0) ? --headY : 640;
        break;
      case 1:
        headX = (headX > 0) ? --headX : 640;
        break;
      case 2:
        headY = (headY < 640) ? ++headY : 0;
        break;
      case 3:
        headX = (headX < 640) ? ++headX : 0;
        break;
      }
      moveData = facing*1000000 + headX*1000 + headY;
      // System.out.println(moveData); // debug
      moveQueue.addLast(moveData);
    }

    @Override
    protected void draw(Graphics2D g2d) {

      g2d.drawImage(sprite[facing], headX, headY, null);

      g2d.drawImage(sprite[facing], headX-GridMap.GRID_PIXELS, headY, null);

      g2d.drawImage(sprite[facing], headX+GridMap.GRID_PIXELS, headY, null);

      g2d.drawImage(sprite[facing], headX, headY-GridMap.GRID_PIXELS, null);

      g2d.drawImage(sprite[facing], headX, headY+GridMap.GRID_PIXELS, null);

    }
  }

  private class SnakeBody extends SnakePart {
    private BufferedImage[] sprite;

    private SnakeBody(int facing, SnakePart followee, BufferedImage[] spriteArr) {
      super(facing, followee);
      sprite = spriteArr;
      for(int i = 0; i < 16; i++)
        moveQueue.add(-1);
    }

    @Override
    protected void tick() {
      moveData = moveQueue.peekFirst();
      System.out.println(moveData);
      
      if(moveData != -1) {
        subfacing = moveData / 1000000;
        x = moveData / 1000 % 1000;
        y = moveData % 1000;
      }
      moveQueue.addLast(followee.moveQueue.pollFirst());
    }

    @Override
    protected void draw(Graphics2D g2d) {

      g2d.drawImage(sprite[subfacing%2], x, y, null);

      g2d.drawImage(sprite[subfacing%2], x-GridMap.GRID_PIXELS, y, null);

      g2d.drawImage(sprite[subfacing%2], x+GridMap.GRID_PIXELS, y, null);

      g2d.drawImage(sprite[subfacing%2], x, y-GridMap.GRID_PIXELS, null);

      g2d.drawImage(sprite[subfacing%2], x, y+GridMap.GRID_PIXELS, null);

    }
  }

  private class SnakeTail extends SnakePart {
    private BufferedImage[] sprite;

    private SnakeTail(int facing, SnakePart followee, BufferedImage[] spriteArr) {
      super(facing, followee);
      sprite = spriteArr;
      for(int i = 0; i < 16; i++)
        moveQueue.add(-1);
    }

    @Override
    protected void tick() {
      moveData = moveQueue.pollFirst();
      
      if(moveData != -1) {
        subfacing = moveData / 1000000;
        x = moveData / 1000 % 1000;
        y = moveData % 1000;
      }
      moveQueue.addLast(followee.moveQueue.pollFirst());
    }

    @Override
    protected void draw(Graphics2D g2d) {

      g2d.drawImage(sprite[subfacing], x, y, null);

      g2d.drawImage(sprite[subfacing], x-GridMap.GRID_PIXELS, y, null);

      g2d.drawImage(sprite[subfacing], x+GridMap.GRID_PIXELS, y, null);

      g2d.drawImage(sprite[subfacing], x, y-GridMap.GRID_PIXELS, null);

      g2d.drawImage(sprite[subfacing], x, y+GridMap.GRID_PIXELS, null);

    }
  }
}
