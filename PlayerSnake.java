import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PlayerSnake extends Snake {
  private KeyHandler keyH;
  private double sprintSpeed;
  /**
   * keeping player from spamming
   */
  private int[] markCellPos;

  public PlayerSnake(GridMap gridMap, KeyHandler keyH) {
    super(1);
    normalSpeed = 4.0;
    // normalSpeed = 0.0000001; // debug
    sprintSpeed = 4.7;
    facing = 3;
    this.keyH = keyH;
    markCellPos = new int[2];
  }

  @Override
  public void tick() {
    if(keyH.movementIsHeld[facing]) {
      curSpeed = sprintSpeed;
    } else {
      curSpeed = normalSpeed;
    }
    if(markCellPos[0] != headCellPos[0] || markCellPos[1] != headCellPos[1]) {
      if(keyH.movementIsHeld[0] && !keyH.movementIsHeld[1] && !keyH.movementIsHeld[3]
      && facing != 2 && facing != 0) {
        facing = 0;
        markCellPos[0] = headCellPos[0];
        markCellPos[1] = headCellPos[1];
      }

      if(keyH.movementIsHeld[1] && !keyH.movementIsHeld[0] && !keyH.movementIsHeld[2]
      && facing != 3 && facing != 1) {
        facing = 1;
        markCellPos[0] = headCellPos[0];
        markCellPos[1] = headCellPos[1];
      }
      if(keyH.movementIsHeld[2] && !keyH.movementIsHeld[1] && !keyH.movementIsHeld[3]
      && facing != 0 && facing != 2) {
        facing = 2;
        markCellPos[0] = headCellPos[0];
        markCellPos[1] = headCellPos[1];
      }
      if(keyH.movementIsHeld[3] && !keyH.movementIsHeld[0] && !keyH.movementIsHeld[2]
      && facing != 1 && facing != 3) {
        facing = 3;
        markCellPos[0] = headCellPos[0];
        markCellPos[1] = headCellPos[1];
      }
    }
    super.tick();
  }
  
  @Override
  protected void initSnake() {
    headX = GridMap.cellLayout[10][20][0];
    headY = GridMap.cellLayout[10][20][1];
    snakeList.add(new SnakeHead(facing, headSprite));
    snakeList.add(new SnakeBody(headX-16, headY, facing, snakeList.get(snakeList.size()-1), bodySprite));
    snakeList.add(new SnakeTail(headX-32, headY, facing, snakeList.get(snakeList.size()-1), tailSprite));
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
