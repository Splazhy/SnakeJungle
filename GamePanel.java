import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;

public class GamePanel extends JPanel implements Runnable {
  private static final int FPS = 60;
  protected static State state;
  protected static boolean isDebugging = false;
  private KeyHandler keyH;
  private GraphicUI graphicUI;
  private Thread gameThread;

  protected static List<GameHitbox> hitboxList;
  protected static List<Snake> botList;
  protected GridMap gridMap;
  protected PlayerSnake player;
  protected BotSpawner botSpawner;
  protected Apple apple;
  public GamePanel() {
    keyH = new KeyHandler(this);
    graphicUI = new GraphicUI(this);
    hitboxList = new LinkedList<>();
    botList = new LinkedList<>();

    setLayout(null);
    setPreferredSize(new Dimension(800, 640));
    setDoubleBuffered(true);
    setBackground(new ColorUIResource(24, 34, 40));
    addKeyListener(keyH);
    setFocusable(true);
  }

  protected void start() {
    state = State.MENU;
    gameThread = new Thread(this);
    gameThread.start();
    SoundEffects.playMusic();
  }
  
  @Override
  public void run() {

    double interval = 1_000_000_000/FPS;
    double deltaTime = 0;
    long lastTime = System.nanoTime();
    long curTime;

    while(gameThread.isAlive()) {

      curTime = System.nanoTime();
      deltaTime += (curTime - lastTime) / interval;
      lastTime = System.nanoTime();

      if(deltaTime >= 1) {
        update();
        repaint();
        --deltaTime;
      }
      
    }
  }

  protected void load() {
    // System.out.println("lesss go!"); // debug
    gridMap = new GridMap();
    player = new PlayerSnake(gridMap, keyH);
    botSpawner = new BotSpawner(this);
    apple = new Apple(gridMap);
    Score.restart();
    setBackground(Color.BLACK);
    state = State.PLAYZONE;
  }

  protected void unload() {
    gridMap = null;
    player = null;
    botSpawner = null;
    apple = null;

    hitboxList.clear();
    botList.clear();

    setBackground(new ColorUIResource(24, 34, 40));
    state = State.MENU;
  }

  private void update() {
    if(state == State.PLAYZONE) {
      if(player.isAlive) {
        player.tick();
        apple.tick();
        // for(int i = 0; i < botList.size(); i++) {
        //   botList.get(i).tick();
        // }
        // botSpawner.tick();
      }
      else {
        hitboxList.clear();
        botList.clear();
        SoundEffects.playGameOverSound();
        state = State.GAMEOVER;
      }
    } else if(state == State.LOADING) {
      repaint();
      load();
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    BufferedImage gridImage = new BufferedImage(640, 640, BufferedImage.TYPE_INT_ARGB);
    Graphics2D scaledg2d = (Graphics2D)g;
    Graphics2D g2d = gridImage.createGraphics();

    if(state.screen == 1) {
      gridMap.draw(g2d);
      player.draw(g2d);

      apple.draw(g2d);

      for(Snake s : botList)
        s.draw(g2d);
      
      if(isDebugging) {
          for(GameHitbox r : hitboxList) {
            if(r.ID > 0)
              g2d.setColor(Color.red);
            else
              g2d.setColor(Color.blue);
            g2d.draw(r);
          }
          g2d.setColor(new Color(0, 255, 0, 50));
          GridMap.cellDetails.forEach((k,v) -> {
            if(v.isEmpty()) g2d.fillRect((k/100)*16, (k%100)*16, 16, 16);
          });
      }

      /* draws the whole game grid image */
      scaledg2d.drawImage(gridImage,GridMap.offset[0],GridMap.offset[1],GridMap.size,GridMap.size,null);
    }
    
    graphicUI.drawUI(scaledg2d);

    g2d.dispose();
    scaledg2d.dispose();
  }

  protected void updatePanel() {
    if(gridMap != null)
      gridMap.update();
  }
}
