import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorUIResource;

public class GamePanel extends JPanel implements Runnable {
  private static final int FPS = 60;
  protected static State state;
  protected static boolean isDebugging = false;
  private KeyHandler keyH;
  private GraphicUI graphicUI;
  protected JSlider audioSlider;
  protected int audioIconIdx;
  private Thread gameThread;

  protected static ArrayList<GameHitbox> hitboxList;
  protected static CopyOnWriteArrayList<Snake> botList;
  protected GridMap gridMap;
  protected PlayerSnake player;
  protected BotSpawner botSpawner;
  protected Apple apple;

  public GamePanel() {
    keyH = new KeyHandler(this);
    graphicUI = new GraphicUI(this);
    
    hitboxList = new ArrayList<>();
    botList = new CopyOnWriteArrayList<>();

    audioSlider = new JSlider(JSlider.VERTICAL, 1, 10000, 10000);
    audioIconIdx = 3;
    audioSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        SoundPlayer.volumeVal = (float)(Math.log10(((float)(((JSlider)e.getSource()).getValue())/10000f))) * 20f;
        audioIconIdx = (SoundPlayer.volumeVal == -80.0f) ? 0 
        : (SoundPlayer.volumeVal <= -10.0f) ? 1
        : (SoundPlayer.volumeVal <= -2.0f) ? 2 : 3;
      }
    });
    audioSlider.setMajorTickSpacing(1);
    audioSlider.setBackground(Color.orange);
    audioSlider.setFocusable(false);
    add(audioSlider);
    
    setLayout(null);
    setPreferredSize(new Dimension(800, 640));
    setDoubleBuffered(true);
    setBackground(new ColorUIResource(24, 34, 40));
    addKeyListener(keyH);
    setFocusable(true);
  }

  protected void start() {
    state = State.MENU;
    gameThread = new Thread(this, "GameThread");
    gameThread.start();
    SoundPlayer.playMusic();
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
    audioSlider.setVisible(false);

    hitboxList.clear();
    botList.clear();

    gridMap = new GridMap();
    player = new PlayerSnake(gridMap, keyH);
    botSpawner = new BotSpawner();
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
    audioSlider.setVisible(true);
    state = State.MENU;
  }

  private void update() {
    if(state == State.PLAYZONE) {
      if(player.isAlive) {
        player.tick();
        apple.tick();
        for(Snake s : botList) {
          s.tick();
        }
        botSpawner.tick();
      }
      else {
        SoundPlayer.playGameOverSound();
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
      apple.draw(g2d);
      
      for(Snake s : botList)
        s.draw(g2d);
      
      player.draw(g2d);

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
  }

  protected void updatePanel() {
    if(gridMap != null)
      gridMap.update();
    audioSlider.setBounds(Main.width/20, (int)(Main.height/1.8), Main.width/20, Main.height/3);
  }
}
