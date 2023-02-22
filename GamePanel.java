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

  protected GridMap gridMap;
  protected PlayerSnake player;

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
    while(gameThread.isAlive()) {
      update();
      repaint();
      if(state == State.PLAYZONE) {
        try {
          Thread.sleep(Math.round(21*(-Math.log(player.curSpeed)/Math.log(2))+50));
        } catch(InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  protected void load() {
    // System.out.println("lesss go!"); // debug
    gridMap = new GridMap();
    player = new PlayerSnake(gridMap, keyH);
    setBackground(Color.BLACK);
    state = State.PLAYZONE;
  }

  protected void unload() {
    gridMap = null;
    player = null;
    setBackground(new ColorUIResource(24, 34, 40));
    state = State.MENU;
  }

  private void update() {
    if(state == State.PLAYZONE) {
      player.tick();
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
