import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;


public class BotHungrySnake extends Snake {

  static int[] targetedFood; // idk figure it out
  static int[] markCellPos;
  int xDistance;
	int yDistance;
  int direction;
  int[] directions;
  int hCost;
  int gCost;
  int count;
  int numDirections;

  public BotHungrySnake() {
    super(2);
    facing = 3;
    normalSpeed = 1;
    curSpeed = normalSpeed;
    VALUE = 5;
    targetedFood = new int[2];
    targetedFood[0] = Apple.appleX;
    targetedFood[1] = Apple.appleY;
    direction = facing;
  }

  @Override
  public void tick() {
    if(!(targetedFood[0] == Apple.newAppleX && targetedFood[1] == Apple.newAppleY)){
      targetedFood[0] = Apple.newAppleX;
      targetedFood[1] = Apple.newAppleY;

      List<Node> path = aStar();
		  if (path == null) {
			  numDirections = -1;
			    return;
		  }
		  numDirections = path.size();
		  directions = new int[numDirections];
		  for (int i = 0; i < numDirections; i++) {
			  directions[i] = path.get(i).getDirection();
		  }
    }
    if(directions != null) {
      for(int direction : directions){
        if(facingQ.isEmpty() || (facingQ.peekLast() % 2 != direction % 2)) {
          facingQ.add(direction);
          break;
        } 
      }
    }
    
    
    super.tick();
  }
  
  @Override
  protected void initSnake() {
    headX = 0; 
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

  private int findHCost(int xAxis, int yAxis) {
		hCost = 0;
		xDistance = Math.abs(Apple.appleX - xAxis);
		yDistance = Math.abs(Apple.appleY - yAxis);
		if (yDistance != 0) {
			hCost = 4;
		}
		hCost += (xDistance * 10) + (yDistance * 10);
		return hCost;
	}

  private List<Node> aStar() {

		List<Node> parents = new ArrayList<Node>();
		PriorityQueue<Node> open = new PriorityQueue<Node>();
		List<Node> closed = new ArrayList<Node>();

    count = 0;
		gCost = 0;
		Node startNode = new Node(headCellPos[0], headCellPos[1], gCost, findHCost(headCellPos[0], headCellPos[1]));
		startNode.setDirection(direction);
		Node goalNode = new Node(targetedFood[0], targetedFood[1], findHCost(headCellPos[0], headCellPos[1]), 0);

    open.add(startNode);

    while(!open.isEmpty()){

      count++;
			
			Node current = open.poll();
			current.close();
			closed.add(current);

      
      if (current.same(goalNode)) {
				boolean finished = false;
				Node n = current;
				while (!finished) {
					parents.add(n);
					n = n.getParent();
					if (n.getParent() == null) {
						finished = true;
					}
				}
				return parents;
			}

      for (int i = 0; i < 3; i++) {
        if (i == 0) {
					gCost = 10; 
				} else {
					gCost = 14; // if change direction, costs more
				}

        boolean exists = false;
				Node n;
				if (i == 0) {
					if (current.getDirection() == 1) { // Continue Left
						if (GridMap.freeCells.contains((current.getxAxis() - 1 )*100 + current.getyAxis())){
							n = new Node(current.getxAxis() - 1, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}

          }else if (current.getDirection() == 3) { // Continue Right
						if (GridMap.freeCells.contains((current.getxAxis() + 1 )*100 + current.getyAxis())) {
							n = new Node(current.getxAxis() + 1, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
          } else if (current.getDirection() == 0) { // Continue Up
						if (GridMap.freeCells.contains((current.getxAxis()*100 + current.getyAxis() - 1))) {
							n = new Node(current.getxAxis(), current.getyAxis() -1, gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
          } else { // Continue Down
						if(GridMap.freeCells.contains((current.getxAxis()*100 + current.getyAxis() + 1))) {
							n = new Node(current.getxAxis(), current.getyAxis() + 1, gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
					}

        }
        else if (i == 1) {
          if (current.getDirection() == 1) { // Turn Up
            if(GridMap.freeCells.contains((current.getxAxis()*100 + current.getyAxis() - 1))) {
              n = new Node(current.getxAxis(), current.getyAxis() - 1, gCost, findHCost(current.getxAxis(), current.getyAxis()));
              if (open.contains(n) || closed.contains(n)) {
                exists = true;
              }
            } else {
              continue;
            }
          } else if (current.getDirection() == 3) { // Turn Down
            if(GridMap.freeCells.contains((current.getxAxis()*100 + current.getyAxis() + 1))) {
              n = new Node(current.getxAxis(), current.getyAxis() + 1, gCost, findHCost(current.getxAxis(), current.getyAxis()));
              if (open.contains(n) || closed.contains(n)) {
                exists = true;
              }
            } else {
              continue;
            }
          } else if (current.getDirection() == 2) { // Turn Left
            if(GridMap.freeCells.contains(((current.getxAxis() - 1)*100 + current.getyAxis()))) {
              n = new Node(current.getxAxis() - 1, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
              if (open.contains(n) || closed.contains(n)) {
                exists = true;
              }
            } else {
              continue;
            }
          } else { // Turn Right
            if(GridMap.freeCells.contains(((current.getxAxis() + 1)*100 + current.getyAxis()))) {
              n = new Node(current.getxAxis() + 1, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
              if (open.contains(n) || closed.contains(n)) {
                exists = true;
              }
            } else {
              continue;
            }
          }
        }else{
          if (current.getDirection() == 1) { // Turn Down
						if(GridMap.freeCells.contains((current.getxAxis()*100 + current.getyAxis() + 1))) {
							n = new Node(current.getxAxis(), current.getyAxis() + 1, gCost, findHCost(current.getxAxis(), current.getyAxis()));
							if (open.contains(n) || closed.contains(n)) {
								exists = true;
							}
						} else {
							continue;
						}
          } else if (current.getDirection() == 3) { // Turn Up
            if(GridMap.freeCells.contains((current.getxAxis()*100 + current.getyAxis() - 1))) {
              n = new Node(current.getxAxis(), current.getyAxis() - 1, gCost, findHCost(current.getxAxis(), current.getyAxis()));
              if (open.contains(n) || closed.contains(n)) {
                exists = true;
              }
            } else {
              continue;
            }
          } else if (current.getDirection() == 2) { // Turn Right
            if(GridMap.freeCells.contains(((current.getxAxis() + 1)*100 + current.getyAxis()))) {
              n = new Node(current.getxAxis() + 1, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
              if (open.contains(n) || closed.contains(n)) {
                exists = true;
              }
            } else {
              continue;
            }
          } else { // Turn Left
            if(GridMap.freeCells.contains(((current.getxAxis() - 1)*100 + current.getyAxis()))) {
              n = new Node(current.getxAxis() - 1, current.getyAxis(), gCost, findHCost(current.getxAxis(), current.getyAxis()));
              if (open.contains(n) || closed.contains(n)) {
                exists = true;
              }
            } else {
              continue;
            }
          }
        }//i==2
        if (exists && n.isClosed()) {
					continue;
				}
				
				if (n.getFCost() <= current.getFCost() || !open.contains(n)) {
					n.setParent(current);
					if (!open.contains(n)) {
						n.setgCost(n.getParent().getgCost() + gCost);
						open.add(n);
					}
				}
			}
		}
		return null;
	}
}