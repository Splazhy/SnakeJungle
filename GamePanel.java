import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;

public class GamePanel extends JPanel implements Runnable {
  protected static State state;
  private KeyHandler keyH;
  private GraphicUI graphicUI;
  private Thread gameThread;
  private static final int FPS = 1200;

  protected GridMap gridMap;
  protected PlayerSnake player;
  protected Apple apple;
  public GamePanel() throws IOException {
    keyH = new KeyHandler(this);
    graphicUI = new GraphicUI(this);

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

    double tickInterval = 1000000000 / FPS;
    double deltaTime = 0;
    long lastTime = System.nanoTime();
    long curTime;

    while(gameThread.isAlive()) {

      curTime = System.nanoTime();
      deltaTime += (curTime - lastTime) / tickInterval;

      if(deltaTime >= 1) {
        update();
        deltaTime--;
      }
      repaint();

      lastTime = curTime;

    }
  }

  protected void load() {
    // System.out.println("lesss go!"); // debug
    gridMap = new GridMap();
    player = new PlayerSnake(gridMap, keyH);
    apple = new Apple(gridMap);
    setBackground(Color.BLACK);
    state = State.PLAYZONE;
  }

  protected void unload() {
    gridMap = null;
    player = null;
    apple = null;
    setBackground(new ColorUIResource(24, 34, 40));
    state = State.MENU;
  }

  private void update() {
    if(state == State.PLAYZONE) {
      player.tick();
      apple.tick();
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
      scaledg2d.drawImage(gridImage,GridMap.offset[0],GridMap.offset[1],GridMap.size,GridMap.size,null);
    }
    
    graphicUI.drawUI(scaledg2d);

    g2d.dispose();
    scaledg2d.dispose();
  }

  protected void updatePanel() {
    gridMap.update();
  }
}
