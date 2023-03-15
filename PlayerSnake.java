import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PlayerSnake extends Snake {
  private KeyHandler keyH;
  private int sprintSpeed;
  private Integer newFacing;

  public PlayerSnake(GridMap gridMap, KeyHandler keyH) {
    super(1);
    normalSpeed = 1;
    sprintSpeed = 3;
    this.keyH = keyH;
  }

  @Override
  public void tick() {
    {
      boolean isAllFalse = true;
      for(int i = 0; i < keyH.movementIsHeld.length; i++) {
        if(keyH.movementIsHeld[i] && i != (facing+2) % 4) {
          isAllFalse = false;
          break;
        }
      }
      if(isAllFalse)
        curSpeed = normalSpeed;
      else
        curSpeed = sprintSpeed;
    }
    newFacing = null;
    if(keyH.movementIsHeld[0] && !keyH.movementIsHeld[1] && !keyH.movementIsHeld[3]
    && facing != 2 && facing != 0) {
      newFacing = 0;
    }
    if(keyH.movementIsHeld[1] && !keyH.movementIsHeld[0] && !keyH.movementIsHeld[2]
    && facing != 3 && facing != 1) {
      newFacing = 1;
    }
    if(keyH.movementIsHeld[2] && !keyH.movementIsHeld[1] && !keyH.movementIsHeld[3]
    && facing != 0 && facing != 2) {
      newFacing = 2;
    }
    if(keyH.movementIsHeld[3] && !keyH.movementIsHeld[0] && !keyH.movementIsHeld[2]
    && facing != 1 && facing != 3) {
      newFacing = 3;
    }
    if(newFacing != null && (facingQ.isEmpty()
      || (!facingQ.isEmpty() && facingQ.peek() % 2 != newFacing % 2))) {
        facingQ.add(newFacing);
    }
    super.tick(); // lmao
  }
  
  @Override
  protected void initSnake() {
    headX = -16;
    headY = GridMap.cellLayout[10][20][1];
    partList.add(new SnakeHead(facing, headSprite));
    partList.add(new SnakeBody(headX, headY, facing, partList.get(partList.size()-1), bodySprite));
    partList.add(new SnakeTail(headX, headY, facing, partList.get(partList.size()-1), tailSprite));
  }
  @Override
  protected void loadSprite() {
    try {
      headSprite[0] = ImageIO.read(new File("sprites/snake/playerhead_up.png"));
      headSprite[1] = ImageIO.read(new File("sprites/snake/playerhead_left.png"));
      headSprite[2] = ImageIO.read(new File("sprites/snake/playerhead_down.png"));
      headSprite[3] = ImageIO.read(new File("sprites/snake/playerhead_right.png"));

      bodySprite[0] = ImageIO.read(new File("sprites/snake/playerbody_vertical.png"));
      bodySprite[1] = ImageIO.read(new File("sprites/snake/playerbody_horizontal.png"));
      bodySprite[2] = ImageIO.read(new File("sprites/snake/playerbody_UL.png"));
      bodySprite[3] = ImageIO.read(new File("sprites/snake/playerbody_UR.png"));
      bodySprite[4] = ImageIO.read(new File("sprites/snake/playerbody_DL.png"));
      bodySprite[5] = ImageIO.read(new File("sprites/snake/playerbody_DR.png"));

      tailSprite[0] = ImageIO.read(new File("sprites/snake/playertail_up.png"));
      tailSprite[1] = ImageIO.read(new File("sprites/snake/playertail_left.png"));
      tailSprite[2] = ImageIO.read(new File("sprites/snake/playertail_down.png"));
      tailSprite[3] = ImageIO.read(new File("sprites/snake/playertail_right.png"));
    } catch(IOException e) {
      e.printStackTrace();
    }
  }
  /**
   * for debugging purpose
   */
  @Override
  public String toString() {
    return String.format("[%d,%d]",headX,headY);
  }
}
