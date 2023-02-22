import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PlayerSnake extends Snake {
  private KeyHandler keyH;
  private boolean isSprinting;
  private double sprintSpeed;

  public PlayerSnake(GridMap gridMap, KeyHandler keyH) {
    super();
    normalSpeed = 4.0;
    // normalSpeed = 1.0; // debug
    sprintSpeed = 4.7;
    facing = 3;
    this.keyH = keyH;
    isSprinting = false;
    cellPos = GridMap.getCellPos(headX, headY);
  }

  @Override
  public void tick() {
    if(keyH.movementIsHeld[facing]) {
      isSprinting = true;
    } else {
      isSprinting = false;
    }
    if(isSprinting) {
      curSpeed = sprintSpeed;
    } else {
      curSpeed = normalSpeed;
    }
    if(keyH.movementIsHeld[0] && !keyH.movementIsHeld[1] && !keyH.movementIsHeld[3]) {
      headX = cellPos[0][0];
      facing = 0;
    }
    if(keyH.movementIsHeld[1] && !keyH.movementIsHeld[0] && !keyH.movementIsHeld[2]) {
      headY = cellPos[0][1];
      facing = 1;
    }
    if(keyH.movementIsHeld[2] && !keyH.movementIsHeld[1] && !keyH.movementIsHeld[3]) {
      headX = cellPos[0][0];
      facing = 2;
    }
    if(keyH.movementIsHeld[3] && !keyH.movementIsHeld[0] && !keyH.movementIsHeld[2]) {
      headY = cellPos[0][1];
      facing = 3;
    }
    super.tick();
  }
  
  @Override
  protected void initSnake() {
    headX = GridMap.cellLayout[20][10][0];
    headY = GridMap.cellLayout[20][10][1];
    snakeList.add(new SnakeHead(facing, headSprite));
    for(int i = 0; i < 0; i++)
      snakeList.add(new SnakeBody(headX, headY, facing, snakeList.get(i), bodySprite));
    snakeList.add(new SnakeTail(headX, headY, facing, snakeList.get(snakeList.size()-1), tailSprite));
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
