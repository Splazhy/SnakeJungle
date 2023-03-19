import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class BotSpawner {
  private static Queue<Snake> botQueue;
  private Random rng;
  private long beginTime;
  private long targetTime;

  protected BotSpawner() {
    botQueue = new LinkedList<>();
    rng = new Random();
    beginTime = System.nanoTime();
    targetTime = (rng.nextInt(10)+20) * 1_000_000_000L;
  }

  protected void spawn(Snake snake) {
    botQueue.add(snake);
  }

  protected void tick() {
    if(System.nanoTime() - beginTime >= targetTime) {
      int botNum = rng.nextInt(4)+1;
      for(int i = 0; i < botNum; i++)
        botQueue.add(new BotFrenzySnake());
      beginTime = System.nanoTime();
      targetTime = (rng.nextInt(10)+20) * 1_000_000_000L;
    }
    if(!botQueue.isEmpty())
      GamePanel.botList.add(botQueue.poll());
  }
}
