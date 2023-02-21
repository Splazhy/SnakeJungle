import java.awt.Color;
import java.awt.Dimension;
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
      if(player != null) {
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
  }

  protected void unload() {
    gridMap = null;
    player = null;
    setBackground(new ColorUIResource(24, 34, 40));
  }

  private void update() {
    if(state == State.PLAYZONE && player != null) {
      player.tick();
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;

    if(gridMap != null)
      gridMap.draw(g2d);
    if(player != null)
      player.draw(g2d);
    graphicUI.drawUI(g2d);
    
    g2d.dispose();
  }

  protected void updatePanel() {
    if(player != null && gridMap != null) {
      gridMap.update();
      player.calibratePosition();
    }
  }
}
