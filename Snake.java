import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
   * score to add when certain snake died
   */
  protected int VALUE;
  /**
   * Head position (top-left point)<p>
   * ตำแหน่งซ้ายบนของหัวงู
   */
  protected int headX, headY;
  protected int curSpeed;
  protected int normalSpeed;
  protected boolean drawWrap;
  protected boolean isAlive;
  protected boolean isEating;
  /**
   * 
   */
  protected ArrayList<SnakePart> partList;
  private List<GameHitbox> partHitbox;
  /**
   * GridMap cell coordinates<p>
   * พิกัดช่องบน GridMap 
   */
  protected int[] headCellPos; // {x,y} of grid of head
  /**
   * 0 for UP <p>
   * 1 for LEFT <p>
   * 2 for DOWN <p>
   * 3 for RIGHT <p>
   */
  protected int facing;
  protected Queue<Integer> facingQ;
  protected HashMap<Integer, Integer> turnPointMap;

  protected BufferedImage[] headSprite;
  protected BufferedImage[] bodySprite;
  protected BufferedImage[] tailSprite;

  public Snake(int ID) {
    this.ID = ID;
    VALUE = 5;
    normalSpeed = 1;
    curSpeed = normalSpeed;
    facing = 3;
    facingQ = new LinkedList<>();
    drawWrap = false;
    isAlive = true;
    isEating = false;
    headSprite = new BufferedImage[4];
    bodySprite = new BufferedImage[6];
    tailSprite = new BufferedImage[4];
    partList = new ArrayList<>();
    partHitbox = new LinkedList<>();
    turnPointMap = new HashMap<>();
    loadSprite();
    initSnake();
    headCellPos = getCellPos(headX, headY, facing);
  }

  protected abstract void initSnake();
  protected abstract void loadSprite();

  protected void tick() {
    if(!isAlive) {
      if(!(this instanceof PlayerSnake))
        Score.addScore(VALUE);
      GamePanel.hitboxList.removeAll(partHitbox);
      GamePanel.botList.remove(this);
    }
    for(int i = 0; i < partList.size(); i++) {
      partList.get(i).tick();
    }
    for(GameHitbox r : GamePanel.hitboxList) {
      if(!r.equals(partHitbox.get(0)) && partHitbox.get(0).intersects(r.getBounds2D()) && r.ID > 0)
        isAlive = false;
      else if(!r.equals(partHitbox.get(0)) && partHitbox.get(0).intersects(r.getBounds2D()) && r.ID < 0){
        isEating = true;
      }
    }
    if(isEating) {
      if(this instanceof PlayerSnake)
        Score.addScore(1);
      grow(1);
      isEating = false;
      Apple.respawnApple();
    }
  }

  protected void draw(Graphics2D g2d) {
    for(int i = 0; i < partList.size(); i++) {
      partList.get(i).draw(g2d);
    }
  }

  protected void grow(int n) {
    for(int i = 0; i < n; i++) {
      SnakeBody body = new SnakeBody(partList.get(partList.size()-2).x
      , partList.get(partList.size()-2).y
      , partList.get(partList.size()-2).subFacing
      , partList.get(partList.size()-2), bodySprite);
      partList.add(partList.size()-1,body);
      partList.get(partList.size()-1).followee = body;
    }
  }

  private int[] getCellPos(int x, int y, int tmpFacing) {
    switch(tmpFacing) {
      case 0: case 1:
        return new int[] {Math.abs(x/16) % 40, Math.abs(y/16) % 40};
      default:// case down: case right:
        int tmpX = Math.abs(x/16), tmpY = Math.abs(y/16);
        if(x % 16 != 0) ++tmpX;
        if(y % 16 != 0) ++tmpY;
        return new int[] {tmpX % 40, tmpY % 40};
    }
  }
  
  /**
   * HEAD, BODY, TAIL
   */
  protected abstract class SnakePart {
    protected int x, y;
    protected int subFacing;
    protected int[] subCellPos;
    protected SnakePart followee;
    protected GameHitbox hitbox;

    protected SnakePart(int x, int y, int facing, SnakePart followee) {
      this.x = x; this.y = y;
      subFacing = facing;
      subCellPos = getCellPos(x, y, subFacing);
      this.followee = followee;
      hitbox = new GameHitbox(x+2,y+2,10,10, ID);
      partHitbox.add(hitbox);
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
      for(int i = 0; i < curSpeed; i++) {
        if(!facingQ.isEmpty() && headX % 16 == 0 && headY % 16 == 0) {
          facing = facingQ.poll();
          turnPointMap.put(headX*1000 + headY, facing);
        }
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
          headY = (headY < 624) ? ++headY : -16;
          break;
        case 3:
          headX = (headX < 624) ? ++headX : -16;
          headY = GridMap.cellLayout[0][headCellPos[1]][1];
          break;
        }
        headCellPos = getCellPos(headX, headY, facing);
      }
      x = headX; y = headY;
      subFacing = facing;
      switch(facing) {
        case 0: hitbox.setFrame(headX+5, headY, 4, 4); break;
        case 1: hitbox.setFrame(headX, headY+5, 4, 4); break;
        case 2: hitbox.setFrame(headX+5, headY+10, 4, 4); break;
        case 3: hitbox.setFrame(headX+10, headY+5, 4, 4); break;
      }
    }

    @Override
    protected void draw(Graphics2D g2d) {

      g2d.drawImage(sprite[facing], headX, headY, null);

      if(drawWrap) {
        g2d.drawImage(sprite[facing], headX-GridMap.GRID_PIXELS, headY, null);

        g2d.drawImage(sprite[facing], headX+GridMap.GRID_PIXELS, headY, null);

        g2d.drawImage(sprite[facing], headX, headY-GridMap.GRID_PIXELS, null);

        g2d.drawImage(sprite[facing], headX, headY+GridMap.GRID_PIXELS, null);
      }
    }
  }

  protected class SnakeBody extends SnakePart {
    private BufferedImage[] sprite;

    protected SnakeBody(int x, int y, int facing, SnakePart followee, BufferedImage[] spriteArr) {
      super(x, y, facing, followee);
      sprite = spriteArr;
    }

    @Override
    protected void tick() {
      int dist = Math.min(Math.abs(followee.x - x) + Math.abs(followee.y - y)
      , Math.min(Math.abs(followee.x - (x+640)) + Math.abs(followee.y - y)
      , Math.min(Math.abs(followee.x - (x-640)) + Math.abs(followee.y - y)
      , Math.min(Math.abs(followee.x - x) + Math.abs(followee.y - (y+640))
      , Math.abs(followee.x - x) + Math.abs(followee.y - (y-640))))));
      if(dist > 16) {
        for(int i = 0; i < (dist-16); i++) {
          if(turnPointMap.containsKey(x*1000 + y)) {
            subFacing = turnPointMap.get(x*1000 + y);
          }
          switch(subFacing) {
            case 0:
              x = GridMap.cellLayout[subCellPos[0]][0][0];
              y = (y > 0) ? --y : 640;
              break;
            case 1:
              x = (x > 0) ? --x : 640;
              y = GridMap.cellLayout[0][subCellPos[1]][1];
              break;
            case 2:
              x = GridMap.cellLayout[subCellPos[0]][0][0];
              y = (y < 624) ? ++y : -16;
              break;
            case 3:
              x = (x < 624) ? ++x : -16;
              y = GridMap.cellLayout[0][subCellPos[1]][1];
              break;
          }
          subCellPos = getCellPos(x, y, subFacing);
        }
        hitbox.setFrame(x+2, y+2, 10, 10);
      } else if(followee instanceof SnakeHead || dist < 2) {
        hitbox.setFrame(x+2, y+2, 0, 0);
      } else if(dist >= 2) {
        hitbox.setFrame(x+2, y+2, 10, 10);
      }
    }

    @Override
    protected void draw(Graphics2D g2d) {

      g2d.drawImage(sprite[subFacing%2], x, y, null);

      if(drawWrap) {
        
        g2d.drawImage(sprite[subFacing%2], x-GridMap.GRID_PIXELS, y, null);
  
        g2d.drawImage(sprite[subFacing%2], x+GridMap.GRID_PIXELS, y, null);
  
        g2d.drawImage(sprite[subFacing%2], x, y-GridMap.GRID_PIXELS, null);
  
        g2d.drawImage(sprite[subFacing%2], x, y+GridMap.GRID_PIXELS, null);

      }
    }
  }

  protected class SnakeTail extends SnakePart {
    private BufferedImage[] sprite;

    protected SnakeTail(int x, int y, int facing, SnakePart followee, BufferedImage[] spriteArr) {
      super(x, y, facing, followee);
      sprite = spriteArr;
    }

    @Override
    protected void tick() {
      int dist = Math.min(Math.abs(followee.x - x) + Math.abs(followee.y - y)
      , Math.min(Math.abs(followee.x - (x+640)) + Math.abs(followee.y - y)
      , Math.min(Math.abs(followee.x - (x-640)) + Math.abs(followee.y - y)
      , Math.min(Math.abs(followee.x - x) + Math.abs(followee.y - (y+640))
      , Math.abs(followee.x - x) + Math.abs(followee.y - (y-640))))));
      if(dist > 16) {
        drawWrap = true;
      for(int i = 0; i < (dist-16); i++) {
        if(turnPointMap.containsKey(x*1000 + y)) {
          subFacing = turnPointMap.get(x*1000 + y);
          turnPointMap.remove(x*1000 + y);
        }
        switch(subFacing) {
          case 0:
            x = GridMap.cellLayout[subCellPos[0]][0][0];
            y = (y > 0) ? --y : 640;
            break;
          case 1:
            x = (x > 0) ? --x : 640;
            y = GridMap.cellLayout[0][subCellPos[1]][1];
            break;
          case 2:
            x = GridMap.cellLayout[subCellPos[0]][0][0];
            y = (y < 624) ? ++y : -16;
            break;
          case 3:
            x = (x < 624) ? ++x : -16;
            y = GridMap.cellLayout[0][subCellPos[1]][1];
            break;
        }
        subCellPos = getCellPos(x, y, subFacing);
      }
        hitbox.setFrame(x+2, y+2, 10, 10);
      } else if(dist < 1) {
        hitbox.setFrame(x+2, y+2, 0, 0);
      }
    }

    @Override
    protected void draw(Graphics2D g2d) {

      g2d.drawImage(sprite[subFacing], x, y, null);

      if(drawWrap) {
        g2d.drawImage(sprite[subFacing], x-GridMap.GRID_PIXELS, y, null);

        g2d.drawImage(sprite[subFacing], x+GridMap.GRID_PIXELS, y, null);

        g2d.drawImage(sprite[subFacing], x, y-GridMap.GRID_PIXELS, null);

        g2d.drawImage(sprite[subFacing], x, y+GridMap.GRID_PIXELS, null);
      }
    }
  }
}
