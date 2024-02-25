import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

public class BotSpawner {
  private BufferedImage warningImage;
  private static Queue<Snake> spawningQueue;
  private static Queue<int[]> warningQueue;
  private Random rng;
  private long beginTime;
  private long targetTime;
  private boolean toDraw;
  private int flickerInterval;

  private int rnd_facing;
  private int rnd_headX;
  private int rnd_headY;

  protected BotSpawner() {
    spawningQueue = new LinkedList<>();
    warningQueue = new LinkedList<>();
    rng = new Random();
    beginTime = System.nanoTime();
    targetTime = (rng.nextInt(10)+10) * 1_000_000_000L;
    // targetTime = 5_000_000_000L;
    toDraw = false;
    flickerInterval = 0;

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
      flickerInterval = 0;
      spawningQueue.addAll(warningQueue.stream()
        .map(x -> new BotFrenzySnake(x[0], x[1], x[2]))
        .collect(Collectors.toList()));
      warningQueue.clear();
      insertRandomPos(rng.nextInt(3)+1);
      beginTime = System.nanoTime();
      targetTime = (rng.nextInt(10)+10) * 1_000_000_000L;
      // targetTime = 5_000_000_000L;
    } else if(System.nanoTime() - beginTime >= targetTime - 3_000_000_000L) {
      toDraw = true;
    }
    if(!spawningQueue.isEmpty()) {
      GamePanel.botList.addAll(spawningQueue);
      spawningQueue.clear();
    }
  }

  private void insertRandomPos(int botNum) {
    for(int i = 0; i < botNum; i++) {
      rnd_facing = rng.nextInt(4);
      switch(rnd_facing) {
        case 0:
          rnd_headX = GridMap.cellLayout[rng.nextInt(40)][0][0];
          rnd_headY = 639;
          break;
        case 1:
          rnd_headX = 639;
          rnd_headY = GridMap.cellLayout[0][rng.nextInt(40)][1];
          break;
        case 2:
          rnd_headX = GridMap.cellLayout[rng.nextInt(40)][0][0];
          rnd_headY = -15;
          break;
        case 3:
          rnd_headX = -15;
          rnd_headY = GridMap.cellLayout[0][rng.nextInt(40)][1];
          break;
      }
      warningQueue.add(new int[] {rnd_facing, rnd_headX, rnd_headY});
    }
  }

  protected void draw(Graphics2D g2d) {
    if(toDraw) {
      g2d.setColor(Color.yellow);
      int[][] arr = warningQueue.stream().map(e -> {
        int x = (e[1] < 0) ? e[1]+15 : (e[1] > 624) ? e[1]-15 : e[1];
        int y = (e[2] < 0) ? e[2]+15 : (e[2] > 624) ? e[2]-15 : e[2];
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
