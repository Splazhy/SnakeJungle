import java.util.LinkedList;
import java.util.Queue;

public class BotSpawner {
  private GamePanel gp;
  private static Queue<Snake> botQueue;
  private int scoreIncreased;
  private int lastScore;

  protected BotSpawner(GamePanel gp) {
    this.gp = gp;
    botQueue = new LinkedList<>();
    scoreIncreased = 0;
    lastScore = 0;
  }

  protected void enqueue(Snake snake) {
    botQueue.add(snake);
  }

  protected void tick() {
    scoreIncreased += Score.getCurScore() - lastScore;
    if(scoreIncreased >= 10) {
      botQueue.add(new BotSquigglySnake(gp.gridMap));
      scoreIncreased = 0;
    }
    addBot();
    lastScore = Score.getCurScore();
  }
  
  protected static void addBot() {
    if(!botQueue.isEmpty())
      GamePanel.botList.add(botQueue.poll());
  }
}
