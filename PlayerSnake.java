import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PlayerSnake extends Snake {
  private KeyHandler keyH;
  protected boolean isSprinting;
  protected double sprintSpeed;

  public PlayerSnake(GridMap gridMap, KeyHandler keyH) {
    super(gridMap);
    normalSpeed = 4.0;
    sprintSpeed = 4.7;
    x = GridMap.cellPos[20][20][0];
    y = GridMap.cellPos[20][20][1];
    facing = 3;
    this.keyH = keyH;
    isSprinting = false;
    getCellPos(x, y);
    try {
      headSprite = new BufferedImage[4];
      headSprite[0] = ImageIO.read(new File("sprites/snake/playerhead_up.png"));
      headSprite[1] = ImageIO.read(new File("sprites/snake/playerhead_left.png"));
      headSprite[2] = ImageIO.read(new File("sprites/snake/playerhead_down.png"));
      headSprite[3] = ImageIO.read(new File("sprites/snake/playerhead_right.png"));

      bodySprite = new BufferedImage[6];

      tailSprite = new BufferedImage[4];
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

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
      x = cellPos[0];
      facing = 0;
    }
    if(keyH.movementIsHeld[1] && !keyH.movementIsHeld[0] && !keyH.movementIsHeld[2]) {
      y = cellPos[1];
      facing = 1;
    }
    if(keyH.movementIsHeld[2] && !keyH.movementIsHeld[1] && !keyH.movementIsHeld[3]) {
      x = cellPos[0];
      facing = 2;
    }
    if(keyH.movementIsHeld[3] && !keyH.movementIsHeld[0] && !keyH.movementIsHeld[2]) {
      y = cellPos[1];
      facing = 3;
    }
    switch(facing) {
      case 0:
      if(y > GridMap.cellPos[0][0][1])
        y--; 
      break;
      case 1:
      if(x > GridMap.cellPos[0][0][0])
        x--;
      break;
      case 2:
      if(y < GridMap.cellPos[39][0][1])
        y++;
      break;
      case 3:
      if(x < GridMap.cellPos[0][39][0])
        x++;
      break;
    }
    getCellPos(x, y);
  }
  
  @Override
  public String toString() {
    return String.format("[%d,%d]",x,y);
  }
}
