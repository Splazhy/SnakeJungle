import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PlayerSnake extends Snake {
  private KeyHandler keyH;
  protected boolean isSprinting;

  /**
   * Snake's head sprites, each member in array is according to {@code Snake.facing}
   * @see Snake
   */
  protected final BufferedImage[] headSprite;
  protected final BufferedImage[] bodySprite;
  protected final BufferedImage[] tailSprite;

  public PlayerSnake(KeyHandler keyH) throws IOException {
    super();
    this.keyH = keyH;
    isSprinting = false;
    speed = 0;

    headSprite = new BufferedImage[4];
    headSprite[0] = ImageIO.read(new File("sprites/snake/playerhead_up.png"));
    headSprite[1] = ImageIO.read(new File("sprites/snake/playerhead_left.png"));
    headSprite[2] = ImageIO.read(new File("sprites/snake/playerhead_down.png"));
    headSprite[3] = ImageIO.read(new File("sprites/snake/playerhead_right.png"));

    bodySprite = new BufferedImage[6];

    tailSprite = new BufferedImage[4];
  }

  public void tick() {
    if(keyH.movementIsHeld[facing]) {
      isSprinting = true;
    } else {
      isSprinting = false;
    }
    if(isSprinting) {
      speed = 7;
    } else {
      speed = 3;
    }
    if(keyH.movementIsHeld[0] && !keyH.movementIsHeld[1] && !keyH.movementIsHeld[3]) {
      x /= 16;
      x *= 16;
      facing = 0;
    }
    if(keyH.movementIsHeld[1] && !keyH.movementIsHeld[0] && !keyH.movementIsHeld[2]) {
      y /= 16;
      y *= 16;
      facing = 1;
    }
    if(keyH.movementIsHeld[2] && !keyH.movementIsHeld[1] && !keyH.movementIsHeld[3]) {
      x /= 16;
      x *= 16;
      facing = 2;
    }
    if(keyH.movementIsHeld[3] && !keyH.movementIsHeld[0] && !keyH.movementIsHeld[2]) {
      y /= 16;
      y *= 16;
      facing = 3;
    }
    switch(facing) {
      case 0:
        if(y > 0)
          y--; 
        break;
      case 1:
        if(x > 0)
          x--;
        break;
      case 2:
        if(y < GamePanel.sizeY-16)
          y++;
        break;
      case 3:
        if(x < GamePanel.sizeX-16)
          x++;
        break;
    }
  }

  @Override
  public String toString() {
    return String.format("[%d,%d]",x,y);
  }
}
