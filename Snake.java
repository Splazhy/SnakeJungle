import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Deque;
import java.util.Random;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Snake in this game moves pixel-by-pixel, not cell-by-cell.<p>
 * งูในเกมนี้ขยับทีละ pixel ไม่ใช่ทีละช่อง
 * @see PlayerSnake
 */
public abstract class Snake {

  protected final int ID;
  protected final int snakeHashCode;
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
  protected int turnFrame;
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
  protected Deque<Integer> facingQ;
  protected Random rng;
  protected HashMap<Integer, Integer> turnPointMap;

  protected BufferedImage[] headSprite;
  protected BufferedImage[] bodySprite;
  protected BufferedImage[][] turnSprite;
  protected BufferedImage[] tailSprite;

  public Snake(int ID) {
    this.ID = ID;
    VALUE = 5;
    normalSpeed = 1;
    curSpeed = normalSpeed;
    facing = 3;
    facingQ = new LinkedList<>();
    turnFrame = 0;
    isAlive = true;
    isEating = false;
    headSprite = new BufferedImage[4];
    bodySprite = new BufferedImage[2];
    turnSprite = new BufferedImage[4][8];
    tailSprite = new BufferedImage[4];
    partList = new ArrayList<>();
    partHitbox = new LinkedList<>();
    turnPointMap = new HashMap<>();
    rng = new Random();
    snakeHashCode = rng.hashCode();
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
      SoundEffects.playBotsDeath();
      GamePanel.hitboxList.removeAll(partHitbox);
      GamePanel.botList.remove(this);
    }
    for(int i = 0; i < partList.size(); i++) {
      partList.get(i).tick();
    }
    for(GameHitbox r : GamePanel.hitboxList) {
      if(!r.equals(partHitbox.get(0)) && partHitbox.get(0).intersects(r.getBounds2D()) && r.ID > 0) {
        isAlive = false;
        VALUE = (r.ID == 1) ? VALUE : 0;
      } else if(!r.equals(partHitbox.get(0)) && partHitbox.get(0).intersects(r.getBounds2D()) && r.ID < 0){
        isEating = true;
      }
    }
    if(isEating) {
      if(this instanceof PlayerSnake){
        Score.addScore(1);
        SoundEffects.playBitingSound();
      }
      else {
        SoundEffects.playBotEatingSound();
      }
      grow(1);
      isEating = false;
      Apple.respawnApple();
    }
  }

  protected void draw(Graphics2D g2d) {
    for(int i = 0; i < partList.size(); i++) {
      partList.get(i).draw(g2d);
    }
    g2d.setColor(Color.MAGENTA);
    turnPointMap.forEach((k, v) -> {
      int turnCase = (v == 30) ? 0 : (v == 23) ? 1 : (v == 1) ? 2
      : (v == 12) ? 3 : (v == 21) ? 4 : (v == 10) ? 5 : (v == 32) ? 6 : 7;
      if(((k/1000)/16) % 2 == 0 ^ ((k%1000)/16) % 2 == 0)
        g2d.setColor(new Color(28, 43, 52));
      else
        g2d.setColor(new Color(24, 34, 40));
      if(turnCase < 4)
        g2d.drawImage(turnSprite[turnCase][turnFrame], k/1000, k%1000, g2d.getColor(), null);
      else
        g2d.drawImage(turnSprite[turnCase%4][7-turnFrame], k/1000, k%1000, g2d.getColor(), null);
    });
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
        return new int[] {(x/16) % 40, (y/16) % 40};
      default:// case down: case right:
        return new int[] {((x+15)/16) % 40, ((y+15)/16) % 40};
    }
  }
  
  /**
   * HEAD, BODY, TAIL
   */
  protected abstract class SnakePart {
    protected int x, y;
    protected int subFacing;
    protected boolean drawWrap;
    protected int[] subCellPos;
    protected SnakePart followee;
    protected GameHitbox hitbox;

    protected SnakePart(int x, int y, int facing, SnakePart followee) {
      this.x = x; this.y = y;
      subFacing = facing;
      drawWrap = false;
      subCellPos = getCellPos(x, y, subFacing);
      this.followee = followee;
      hitbox = new GameHitbox(x+2,y+2,10,10, ID, this);
      partHitbox.add(hitbox);
      GamePanel.hitboxList.add(hitbox);
      GridMap.cellDetails.get(subCellPos[0]*100+subCellPos[1]).add(snakeHashCode);
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
      if(!drawWrap) {
        drawWrap = (x >= 0 && y >= 0 && x <= 624 && y <= 624) ?
        true : false;
      }
      for(int i = 0; i < curSpeed; i++) {
        turnFrame = ++turnFrame % 8;
        if(!facingQ.isEmpty() && headX % 16 == 0 && headY % 16 == 0) {
          int prevFacing = facing;
          facing = facingQ.poll();
          turnPointMap.put(headX*1000 + headY, prevFacing*10 + facing);
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
      }
      switch(facing) {
        case 0: hitbox.setFrame(headX+5, headY, 4, 4); break;
        case 1: hitbox.setFrame(headX, headY+5, 4, 4); break;
        case 2: hitbox.setFrame(headX+5, headY+10, 4, 4); break;
        case 3: hitbox.setFrame(headX+10, headY+5, 4, 4); break;
      }
      x = headX; y = headY;
      subFacing = facing;
      headCellPos = getCellPos(headX, headY, facing);
      GridMap.cellDetails.get(headCellPos[0]*100+headCellPos[1]).add(snakeHashCode);
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
      if(!drawWrap) {
        drawWrap = (x >= 0 && y >= 0 && x <= 624 && y <= 624) ?
        true : false;
      }
      int dist = Math.min(Math.abs(followee.x - x) + Math.abs(followee.y - y)
      , Math.min(Math.abs(followee.x - (x+640)) + Math.abs(followee.y - y)
      , Math.min(Math.abs(followee.x - (x-640)) + Math.abs(followee.y - y)
      , Math.min(Math.abs(followee.x - x) + Math.abs(followee.y - (y+640))
      , Math.min(Math.abs(followee.x - x) + Math.abs(followee.y - (y-640))
      , Math.min(Math.abs(followee.x - (x-640)) + Math.abs(followee.y - (y-640))
      , Math.min(Math.abs(followee.x - (x+640)) + Math.abs(followee.y - (y-640))
      , Math.min(Math.abs(followee.x - (x-640)) + Math.abs(followee.y - (y+640))
      , Math.abs(followee.x - (x+640)) + Math.abs(followee.y - (y+640))))))))));
      if(dist > 16) {
        for(int i = 0; i < (dist-16); i++) {
          if(turnPointMap.containsKey(x*1000 + y)) {
            subFacing = turnPointMap.get(x*1000 + y) % 10;
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
      if(!drawWrap) {
        drawWrap = (x >= 0 && y >= 0 && x <= 624 && y <= 624) ?
        true : false;
      }
      int dist = Math.min(Math.abs(followee.x - x) + Math.abs(followee.y - y)
      , Math.min(Math.abs(followee.x - (x+640)) + Math.abs(followee.y - y)
      , Math.min(Math.abs(followee.x - (x-640)) + Math.abs(followee.y - y)
      , Math.min(Math.abs(followee.x - x) + Math.abs(followee.y - (y+640))
      , Math.min(Math.abs(followee.x - x) + Math.abs(followee.y - (y-640))
      , Math.min(Math.abs(followee.x - (x-640)) + Math.abs(followee.y - (y-640))
      , Math.min(Math.abs(followee.x - (x+640)) + Math.abs(followee.y - (y-640))
      , Math.min(Math.abs(followee.x - (x-640)) + Math.abs(followee.y - (y+640))
      , Math.abs(followee.x - (x+640)) + Math.abs(followee.y - (y+640))))))))));
      if(dist > 16) {
        for(int i = 0; i < (dist-16); i++) {
          if(turnPointMap.containsKey(x*1000 + y)) {
            subFacing = turnPointMap.get(x*1000 + y) % 10;
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
          GridMap.cellDetails.get(subCellPos[0]*100+subCellPos[1]).remove(snakeHashCode);
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
