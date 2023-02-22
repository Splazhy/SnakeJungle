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
    snakeList = new ArrayList<>();
    loadSprite();
    initSnake();
  }

  protected abstract void initSnake();
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

  protected void grow(int n) {
    // TO-DO
    for(int i = 0; i < n; i++) {
      SnakeBody body = new SnakeBody(snakeList.get(snakeList.size()-2).x
      , snakeList.get(snakeList.size()-2).y
      , snakeList.get(snakeList.size()-2).subfacing
      , snakeList.get(snakeList.size()-2), bodySprite);
      snakeList.add(snakeList.size()-1,body);
      snakeList.get(snakeList.size()-1).followee = body;
      for(int j = 0; j < 16; j++)
        snakeList.get(snakeList.size()-1).moveQueue.addFirst(-1);
    }
  }
  
  /**
   * HEAD, BODY, TAIL
   */
  protected abstract class SnakePart {
    protected int x, y;
    protected int subfacing;
    protected int moveData;
    protected SnakePart followee;
    protected Deque<Integer> moveQueue;

    protected SnakePart(int x, int y, int facing, SnakePart followee) {
      this.x = x; this.y = y;
      subfacing = facing;
      this.followee = followee;
      moveQueue = new LinkedList<>();
    }

    protected abstract void tick();
    protected abstract void draw(Graphics2D g2d);

  }

  protected class SnakeHead extends SnakePart {
    private BufferedImage[] sprite;

    protected SnakeHead(int facing, BufferedImage[] spriteArr) {
      super(headX, headY, facing, null);
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
      x = headX; y = headY;
      moveData = facing*1000000 + headX*1000 + headY;
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

  protected class SnakeBody extends SnakePart {
    private BufferedImage[] sprite;

    protected SnakeBody(int x, int y, int facing, SnakePart followee, BufferedImage[] spriteArr) {
      super(x, y, facing, followee);
      sprite = spriteArr;
      for(int i = 0; i < 16; i++)
        moveQueue.add(-1);
    }

    @Override
    protected void tick() {
      moveData = moveQueue.peekFirst();      
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

  protected class SnakeTail extends SnakePart {
    private BufferedImage[] sprite;

    protected SnakeTail(int x, int y, int facing, SnakePart followee, BufferedImage[] spriteArr) {
      super(x, y, facing, followee);
      sprite = spriteArr;
      for(int i = 0; i < 16; i++)
        moveQueue.add(-1);
    }

    @Override
    protected void tick() {
      if(moveQueue.size() > 16 && moveQueue.peekLast() == -1) {
        moveQueue.pollLast();
      }
      moveQueue.addLast(followee.moveQueue.pollFirst());
      moveData = moveQueue.pollFirst();
      
      if(moveData != -1) {
        subfacing = moveData / 1000000;
        x = moveData / 1000 % 1000;
        y = moveData % 1000;
      }
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