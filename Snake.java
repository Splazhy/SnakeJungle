import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Snake in this game moves pixel-by-pixel, not cell-by-cell.<p>
 * งูในเกมนี้ขยับทีละ pixel ไม่ใช่ทีละช่อง
 * @see PlayerSnake
 */
public abstract class Snake {

  protected final int ID;
  /**
   * Head position (top-left point)<p>
   * ตำแหน่งซ้ายบนของหัวงู
   */
  protected int headX, headY;
  protected double curSpeed;
  protected double normalSpeed;
  private static boolean isChecking;
  protected boolean isAlive;
  /**
   * 
   */
  protected ArrayList<SnakePart> snakeList;
  private List<GameHitbox> snakeHitbox;
  /**
   * GridMap cell coordinates<p>
   * พิกัดช่องบน GridMap 
   */
  protected int[] headCellPos; // {x,y} of grid of head
  protected int[][] prevHeadCellPos;
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

  public Snake(int ID) {
    this.ID = ID;
    normalSpeed = 1;
    curSpeed = normalSpeed;
    facing = 3;
    isAlive = true;
    headSprite = new BufferedImage[4];
    bodySprite = new BufferedImage[6];
    tailSprite = new BufferedImage[4];
    snakeList = new ArrayList<>();
    snakeHitbox = new LinkedList<>();
    loadSprite();
    initSnake();
    headCellPos = GridMap.getCellPos(headX, headY);
  }

  protected abstract void initSnake();
  protected abstract void loadSprite();

  protected void tick() {
    if(!isAlive) {
      GamePanel.hitboxList.removeAll(snakeHitbox);
      GamePanel.botList.remove(this);
    }
    for(int i = 0; i < snakeList.size(); i++) {
      snakeList.get(i).tick();
    }
  }

  protected void draw(Graphics2D g2d) {
    for(int i = 0; i < snakeList.size(); i++) {
      snakeList.get(i).draw(g2d);
    }
  }

  protected void grow(int n) {
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
    protected int[] cellPos;
    /**
     * first 3 digits = 
     */
    protected int moveData;
    protected SnakePart followee;
    protected Deque<Integer> moveQueue;
    protected GameHitbox hitbox;

    protected SnakePart(int x, int y, int facing, SnakePart followee) {
      this.x = x; this.y = y;
      subfacing = facing;
      cellPos = new int[2];
      this.followee = followee;
      moveQueue = new LinkedList<>();
      hitbox = new GameHitbox(x+4,y+4,6,6, ID);
      while(isChecking);
      snakeHitbox.add(hitbox);
      GamePanel.hitboxList.add(hitbox);
    }

    protected abstract void tick();
    protected abstract void draw(Graphics2D g2d);

  }

  protected class SnakeHead extends SnakePart {
    private BufferedImage[] sprite;

    protected SnakeHead(int facing, BufferedImage[] spriteArr) {
      super(headX, headY, facing, null);
      switch(facing) {
        case 0: hitbox.setFrame(headX+5, headY, 4, 4); break;
        case 1: hitbox.setFrame(headX, headY+5, 4, 4); break;
        case 2: hitbox.setFrame(headX+5, headY+10, 4, 4); break;
        case 3: hitbox.setFrame(headX+10, headY+5, 4, 4); break;
      }
      sprite = spriteArr;
    }

    @Override
    protected void tick() {
      switch(facing) {
      case 0:
        headX = GridMap.cellLayout[headCellPos[0]][0][0];
        headY = (headY > 0) ? --headY : 640;
        break;
      case 1:
        headX = (headX > 0) ? --headX : 640;
        headY = GridMap.cellLayout[0][headCellPos[1]][1];
        break;
      case 2:
        headX = GridMap.cellLayout[headCellPos[0]][0][0];
        headY = (headY < 640) ? ++headY : 0;
        break;
      case 3:
        headX = (headX < 640) ? ++headX : 0;
        headY = GridMap.cellLayout[0][headCellPos[1]][1];
        break;
      }
      x = headX; y = headY;
      headCellPos = GridMap.getCellPos(headX, headY);
      switch(facing) {
        case 0: hitbox.setFrame(headX+5, headY, 4, 4); break;
        case 1: hitbox.setFrame(headX, headY+5, 4, 4); break;
        case 2: hitbox.setFrame(headX+5, headY+10, 4, 4); break;
        case 3: hitbox.setFrame(headX+10, headY+5, 4, 4); break;
      }
      isChecking = true;
      for(GameHitbox r : GamePanel.hitboxList) {
        if(!r.equals(hitbox) && hitbox.intersects(r.getBounds2D()) && r.ID > 0)
          isAlive = false;
      }
      isChecking = false;
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
      cellPos = GridMap.getCellPos(x, y);
      hitbox.setFrame(x+2, y+2, 10, 10);
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
      cellPos = GridMap.getCellPos(x, y);
      hitbox.setFrame(x+2, y+2, 10, 10);
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