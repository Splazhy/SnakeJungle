
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class BotSquigglySnake extends Snake {

  private int[] markCellPos;
  private boolean isDown;
  private Random rnd;
  private int randomDist;

  public BotSquigglySnake(GridMap gridMap) {
    super(2);
    normalSpeed = 1;
    curSpeed = normalSpeed;
    VALUE = 10;
    markCellPos = new int[2];
    markCellPos[0] = headCellPos[0]; // test
    markCellPos[1] = headCellPos[1]; // test
    rnd = new Random(); // test
    randomDist = rnd.nextInt(10)+3;
  }

  @Override
  public void tick() {
    int xDist = Math.min(Math.abs(markCellPos[0] - headCellPos[0])
    , Math.min(Math.min(Math.abs(markCellPos[0]+40 - headCellPos[0])
    , Math.abs(markCellPos[0] - headCellPos[0]+40))
    , Math.min(Math.abs(markCellPos[0]-40 - headCellPos[0])
    , Math.abs(markCellPos[0] - headCellPos[0]-40))));
    int yDist = Math.min(Math.abs(markCellPos[1] - headCellPos[1])
    , Math.min(Math.min(Math.abs(markCellPos[1]+40 - headCellPos[1])
    , Math.abs(markCellPos[1] - headCellPos[1]+40))
    , Math.min(Math.abs(markCellPos[1]-40 - headCellPos[1])
    , Math.abs(markCellPos[1] - headCellPos[1]-40))));
    if(markCellPos[0] != headCellPos[0] || markCellPos[1] != headCellPos[1]) {
      switch(facing) {
      case 0:
        if(yDist >= randomDist) {
          facingQ.add(3);
          markCellPos[0] = headCellPos[0];
          markCellPos[1] = headCellPos[1];
          isDown = true;
        }
        break;
      case 2:
        if(yDist >= randomDist) {
          facingQ.add(3);
          markCellPos[0] = headCellPos[0];
          markCellPos[1] = headCellPos[1];
          isDown = false;
        }
        break;
      case 3:
        if(xDist >= 1) {
          facingQ.add((isDown) ? 2 : 0);
          markCellPos[0] = headCellPos[0];
          markCellPos[1] = headCellPos[1];
          randomDist = rnd.nextInt(10)+3;
        }
        break;
      }
    }
    super.tick();
  }
  
  @Override
  protected void initSnake() {
    headX = -16;
    headY = GridMap.cellLayout[20][25][1];
    partList.add(new SnakeHead(facing, headSprite));
    partList.add(new SnakeBody(headX, headY, facing, partList.get(partList.size()-1), bodySprite));
    partList.add(new SnakeTail(headX, headY, facing, partList.get(partList.size()-1), tailSprite));
    grow(30);
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
}
