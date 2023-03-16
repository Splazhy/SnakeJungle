import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BotHungrySnake extends Snake {

  private int[] targetedFood; // idk figure it out

  public BotHungrySnake(GridMap gridMap) {
    super(2);
    normalSpeed = 1;
    curSpeed = normalSpeed;
    VALUE = 5;
  }

  @Override
  public void tick() {
    // TODO: Artificial Intelligence
    super.tick();
  }
  
  @Override
  protected void initSnake() {
    headX = -16; 
    headY = GridMap.cellLayout[0][rng.nextInt(40)][1];
    partList.add(new SnakeHead(facing, headSprite));
    partList.add(new SnakeBody(headX, headY, facing, partList.get(partList.size()-1), bodySprite));
    partList.add(new SnakeTail(headX, headY, facing, partList.get(partList.size()-1), tailSprite));
    grow(30);
  }
  @Override
  protected void loadSprite() {
    try {
      headSprite[0] = ImageIO.read(new File("sprites/snake/player/head_up.png"));
      headSprite[1] = ImageIO.read(new File("sprites/snake/player/head_left.png"));
      headSprite[2] = ImageIO.read(new File("sprites/snake/player/head_down.png"));
      headSprite[3] = ImageIO.read(new File("sprites/snake/player/head_right.png"));

      bodySprite[0] = ImageIO.read(new File("sprites/snake/player/body_vertical.png"));
      bodySprite[1] = ImageIO.read(new File("sprites/snake/player/body_horizontal.png"));
      
      for(int i = 0; i <= 7; i++) {
        turnSprite[0][i] = ImageIO.read(new File(String.format("sprites/snake/player/turn_UL%d.png",i)));
        turnSprite[1][i] = ImageIO.read(new File(String.format("sprites/snake/player/turn_UR%d.png",i)));
        turnSprite[2][i] = ImageIO.read(new File(String.format("sprites/snake/player/turn_DL%d.png",i)));
        turnSprite[3][i] = ImageIO.read(new File(String.format("sprites/snake/player/turn_DR%d.png",i)));
      }

      tailSprite[0] = ImageIO.read(new File("sprites/snake/player/tail_up.png"));
      tailSprite[1] = ImageIO.read(new File("sprites/snake/player/tail_left.png"));
      tailSprite[2] = ImageIO.read(new File("sprites/snake/player/tail_down.png"));
      tailSprite[3] = ImageIO.read(new File("sprites/snake/player/tail_right.png"));
    } catch(IOException e) {
      e.printStackTrace();
    }
  }
}
