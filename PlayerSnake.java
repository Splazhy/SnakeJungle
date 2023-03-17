
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
    if(!keyH.movementIsHeld[0] && !keyH.movementIsHeld[1]
      && !keyH.movementIsHeld[2] && !keyH.movementIsHeld[3])
      curSpeed = normalSpeed;
    else if(!keyH.movementIsHeld[(facing+2)%4])
      curSpeed = sprintSpeed;
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
      || (facingQ.peekLast() % 2 != newFacing % 2))) {
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
      String folderName = "player";
      headSprite[0] = ImageIO.read(new File(String.format("sprites/snake/%s/head_up.png",folderName)));
      headSprite[1] = ImageIO.read(new File(String.format("sprites/snake/%s/head_left.png",folderName)));
      headSprite[2] = ImageIO.read(new File(String.format("sprites/snake/%s/head_down.png",folderName)));
      headSprite[3] = ImageIO.read(new File(String.format("sprites/snake/%s/head_right.png",folderName)));

      bodySprite[0] = ImageIO.read(new File(String.format("sprites/snake/%s/body_vertical.png",folderName)));
      bodySprite[1] = ImageIO.read(new File(String.format("sprites/snake/%s/body_horizontal.png",folderName)));

      for(int i = 0; i <= 7; i++) {
        turnSprite[0][i] = ImageIO.read(new File(String.format("sprites/snake/%s/turn_UL%d.png",folderName,i)));
        turnSprite[1][i] = ImageIO.read(new File(String.format("sprites/snake/%s/turn_UR%d.png",folderName,i)));
        turnSprite[2][i] = ImageIO.read(new File(String.format("sprites/snake/%s/turn_DL%d.png",folderName,i)));
        turnSprite[3][i] = ImageIO.read(new File(String.format("sprites/snake/%s/turn_DR%d.png",folderName,i)));
      }

      tailSprite[0] = ImageIO.read(new File(String.format("sprites/snake/%s/tail_up.png",folderName)));
      tailSprite[1] = ImageIO.read(new File(String.format("sprites/snake/%s/tail_left.png",folderName)));
      tailSprite[2] = ImageIO.read(new File(String.format("sprites/snake/%s/tail_down.png",folderName)));
      tailSprite[3] = ImageIO.read(new File(String.format("sprites/snake/%s/tail_right.png",folderName)));
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
