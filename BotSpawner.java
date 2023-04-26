import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.imageio.ImageIO;

public class BotSpawner {
  private BufferedImage warningImage;
  private static Queue<Snake> spawningQueue;
  private static Queue<Snake> warningQueue;
  private Random rng;
  private long beginTime;
  private long targetTime;
  private boolean toDraw;
  private int flickerInterval;

  protected BotSpawner() {
    spawningQueue = new LinkedList<>();
    warningQueue = new LinkedList<>();
    rng = new Random();
    beginTime = System.nanoTime();
    targetTime = (rng.nextInt(20)+20) * 1_000_000_000L;
    toDraw = false;
    flickerInterval = 0;
    insertRandomSnakes(warningQueue, rng.nextInt(3)+1);

    try {
      warningImage = ImageIO.read(new File("sprites/icon/warning.png"));
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  protected void spawn(Snake snake) {
    GamePanel.botList.add(snake);
  }

  protected void tick() {
    if(GamePanel.state == State.PAUSE) {
      beginTime = System.nanoTime();
    }
    if(System.nanoTime() - beginTime >= targetTime) {
      toDraw = false;
      spawningQueue.addAll(warningQueue);
      warningQueue.clear();
      insertRandomSnakes(warningQueue, rng.nextInt(3)+1);
      beginTime = System.nanoTime();
      targetTime = (rng.nextInt(20)+20) * 1_000_000_000L;
    } else if(System.nanoTime() - beginTime >= targetTime - 5_000_000_000L) {
      toDraw = true;
    }
    if(!spawningQueue.isEmpty())
      GamePanel.botList.add(spawningQueue.poll());
  }

  private void insertRandomSnakes(Queue<Snake> q, int botNum) {
    for(int i = 0; i < botNum; i++) {
      q.add(new BotFrenzySnake());
    }
  }

  protected void draw(Graphics2D g2d) {
    if(toDraw) {
      g2d.setColor(Color.yellow);
      int[][] arr = warningQueue.stream().map(e -> {
        int x = (e.headX < 0) ? e.headX+16 : e.headX;
        int y = e.headY;
        return new int[] {x, y};
      }).toArray(int[][]::new);
      
      if(flickerInterval < 120) {
        for(int[] pos : arr) {
          g2d.drawImage(warningImage, pos[0], pos[1], null);
        }
      }
      flickerInterval = ++flickerInterval % 240;
    }
  }
}
