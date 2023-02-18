import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;

public class GamePanel extends JPanel implements Runnable {
  protected static STATE state;
  protected static boolean isLoading;
  private KeyHandler keyH;
  private GraphicUI graphicUI;
  private Thread gameThread;

  protected GridMap gridMap;
  protected PlayerSnake player;

  public GamePanel() throws IOException {
    keyH = new KeyHandler(this);
    graphicUI = new GraphicUI(this);

    isLoading = false; // true when ENTER is pressed on STATE.MENU

    setLayout(null);
    setPreferredSize(new Dimension(640, 640));
    setDoubleBuffered(true);
    setBackground(new ColorUIResource(24, 34, 40));
    addKeyListener(keyH);
    setFocusable(true);
  }

  public void start() {
    state = STATE.MENU;
    gameThread = new Thread(this);
    gameThread.start();
  }
  
  @Override
  public void run() {
    while(gameThread.isAlive()) {
      if(isLoading) {
        System.out.println("lesss go!");
        gridMap = new GridMap();
        player = new PlayerSnake(gridMap, keyH);
        isLoading = false;
      }
      update();
      repaint();
      if(player != null) {
        try {
          Thread.sleep(Math.round(21*(-Math.log(player.curSpeed)/Math.log(2))+50));
        } catch(InterruptedException e) {
          e.printStackTrace();
        }
      }
      if(Main.isUpdatingFrameSize)
        updatePanel();
    }
  }

  public void update() {
    if(state == STATE.PLAYZONE && player != null) {
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

  private void updatePanel() {
    if(player != null && gridMap != null) {
      gridMap.update();
      player.calibratePosition();
    }
    Main.isUpdatingFrameSize = false;
  }
}
