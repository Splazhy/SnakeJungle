import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BotFrenzySnake extends Snake {

  private int[] targetedPos;

  public BotFrenzySnake() {
    super(3);
    normalSpeed = 1;
    curSpeed = normalSpeed;
    VALUE = 5;
  }

  @Override
  public void tick() {
    targetedPos = new int[] {Apple.newAppleX, Apple.newAppleY};
    boolean[] surroundings = 
      {(GridMap.cellDetails.containsKey(headCellPos[0]*100+(headCellPos[1]-1))
      && (GridMap.cellDetails.get((headCellPos[0]*100)+(headCellPos[1]-1)).isEmpty()
      || GridMap.cellDetails.get((headCellPos[0]*100)+(headCellPos[1]-1)).contains(Apple.hashCode)))
      ,(GridMap.cellDetails.containsKey((headCellPos[0]-1)*100+headCellPos[1])
      && (GridMap.cellDetails.get((headCellPos[0]-1)*100+headCellPos[1]).isEmpty()
      || GridMap.cellDetails.get((headCellPos[0]-1)*100+headCellPos[1]).contains(Apple.hashCode)))
      ,(GridMap.cellDetails.containsKey((headCellPos[0]*100)+(headCellPos[1]+1))
      && (GridMap.cellDetails.get((headCellPos[0]*100)+(headCellPos[1]+1)).isEmpty()
      || GridMap.cellDetails.get((headCellPos[0]*100)+(headCellPos[1]+1)).contains(Apple.hashCode)))
      ,(GridMap.cellDetails.containsKey((headCellPos[0]+1)*100+headCellPos[1])
      && (GridMap.cellDetails.get((headCellPos[0]+1)*100+headCellPos[1]).isEmpty()
      || GridMap.cellDetails.get((headCellPos[0]+1)*100+headCellPos[1]).contains(Apple.hashCode)))};
    int newFacing = -1;
    if(headCellPos[0] > targetedPos[0] && facing != 1) {
      newFacing = (facing == 3) ? (headCellPos[1] > targetedPos[1]) ?
        0 : 2 : (surroundings[1]) ? 
        1 : -1;
    } else if(headCellPos[0] < targetedPos[0] && facing != 3) {
      newFacing = (facing == 1) ? (headCellPos[1] > targetedPos[1]) ?
        0 : 2 : (surroundings[3]) ? 
        3 : -1;
    } else if(headCellPos[1] > targetedPos[1] && facing != 0) {
      newFacing = (facing == 2) ? (headCellPos[0] > targetedPos[0]) ?
        1 : 3 : (surroundings[0]) ? 
        0 : -1;
    } else if(headCellPos[1] < targetedPos[1] && facing != 2) {
      newFacing = (facing == 0) ? (headCellPos[0] > targetedPos[0]) ?
        1 : 3 : (surroundings[2]) ? 
        2 : -1;
    } 
    if(newFacing == -1) {
      if(!surroundings[facing]) {
        newFacing = (surroundings[(facing+1) % 4]) ? 
        (facing+1) % 4 : (facing+3) % 4;
      }
    } else {
      if(!surroundings[newFacing]) {
        newFacing = (surroundings[(newFacing+1) % 4]) ?
        (newFacing+1) % 4 : (newFacing+3) % 4;
      }
    }
    if(newFacing != -1 && ((facingQ.isEmpty() && facing % 2 != newFacing % 2)
      || (!facingQ.isEmpty() && facingQ.peekLast() % 2 != newFacing % 2))) {
      facingQ.add(newFacing);
    }

    super.tick();
  }
  
  @Override
  protected void initSnake() {
    facing = rng.nextInt(4);
    switch(facing) {
      case 0:
        headX = GridMap.cellLayout[rng.nextInt(40)][0][0];
        headY = 624;
        break;
      case 1:
        headX = 624;
        headY = GridMap.cellLayout[0][rng.nextInt(40)][1];
        break;
      case 2:
        headX = GridMap.cellLayout[rng.nextInt(40)][0][0];
        headY = 0;
        break;
      case 3:
        headX = 0;
        headY = GridMap.cellLayout[0][rng.nextInt(40)][1];
        break;
    }
    partList.add(new SnakeHead(facing, headSprite));
    partList.add(new SnakeBody(headX, headY, facing, partList.get(partList.size()-1), bodySprite));
    partList.add(new SnakeTail(headX, headY, facing, partList.get(partList.size()-1), tailSprite));
    grow(rng.nextInt(30));
  }
  @Override
  protected void loadSprite() {
    try {
      String folderName = "frenzybot";
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
}
