import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class BotSpawner {
  private GamePanel gp;
  private static Queue<Snake> botQueue;
  private Random rng;
  private long beginTime;
  private long targetTime;

  protected BotSpawner(GamePanel gp) {
    this.gp = gp;
    botQueue = new LinkedList<>();
    rng = new Random();
    beginTime = System.nanoTime();
    targetTime = (rng.nextInt(20)+30) * 1_000_000_000L;
  }

  protected void spawn(Snake snake) {
    botQueue.add(snake);
  }

  protected void tick() {
    if(System.nanoTime() - beginTime >= targetTime) {
      botQueue.add(new BotHungrySnake(gp.gridMap));
      beginTime = System.nanoTime();
      targetTime = (rng.nextInt(20)+30) * 1_000_000_000L;
    }
    if(!botQueue.isEmpty())
      GamePanel.botList.add(botQueue.poll());
  }
}
