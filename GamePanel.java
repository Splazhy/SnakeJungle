import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;

public class GamePanel extends JPanel implements Runnable {
  protected static State state;
  private KeyHandler keyH;
  private GraphicUI graphicUI;
  private Thread gameThread;

  protected static List<GameHitbox> hitboxList;
  protected static List<Snake> botList;
  protected GridMap gridMap;
  protected PlayerSnake player;
  protected Apple apple;
  public GamePanel() throws IOException {
    keyH = new KeyHandler(this);
    graphicUI = new GraphicUI(this);
    hitboxList = new LinkedList<>();
    botList = new LinkedList<>();

    setLayout(null);
    setPreferredSize(new Dimension(640, 640));
    setDoubleBuffered(true);
    setBackground(new ColorUIResource(24, 34, 40));
    addKeyListener(keyH);
    setFocusable(true);
  }

  protected void start() {
    state = State.MENU;
    gameThread = new Thread(this);
    gameThread.start();
  }
  
  @Override
  public void run() {

    while(gameThread.isAlive()) {
      update();
      repaint();
      if(state == State.PLAYZONE)
        try {
          // Math.round(21*(-Math.log(player.curSpeed)/Math.log(2))+50) // old dependent game speed
          Thread.sleep(8);
        } catch(InterruptedException e) {
          e.printStackTrace();
        }
    }
  }

  protected void load() {
    // System.out.println("lesss go!"); // debug
    gridMap = new GridMap();
    player = new PlayerSnake(gridMap, keyH);
    apple = new Apple(gridMap);
    botList.add(new BotSquigglySnake(gridMap));
    Score.restart();
    setBackground(Color.BLACK);
    state = State.PLAYZONE;
  }

  protected void unload() {
    gridMap = null;
    player = null;
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
        for(Snake s : botList) {
          s.tick();
        }
      }
      else {
        hitboxList.clear();
        botList.clear();
        state = State.GAMEOVER;
      }
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    BufferedImage gridImage = new BufferedImage(640, 640, BufferedImage.TYPE_INT_ARGB);
    Graphics2D scaledg2d = (Graphics2D)g;
    Graphics2D g2d = gridImage.createGraphics();

    if(state != State.MENU) {
      gridMap.draw(g2d);
      player.draw(g2d);

      apple.draw(g2d);

      for(Snake s : botList)
        s.draw(g2d);
      g2d.setColor(Color.RED);
      for(GameHitbox r : hitboxList) // debug
        g2d.draw(r);

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
