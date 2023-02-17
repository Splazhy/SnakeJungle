import java.awt.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

public class GamePanel extends JPanel implements Runnable {
  public static STATE state;
  protected static boolean isLoading;
  private KeyHandler keyH;
  private GraphicUI graphicUI;
  private Thread gameThread;

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
      if(state == STATE.PLAYZONE) {
        if(isLoading) {
          System.out.println("lesss go!");
          try {
            player = new PlayerSnake(keyH);
          } catch (IOException e) {
            e.printStackTrace();
          }

          isLoading = false;
        }
        update();
      }
      repaint();
      if(player != null) {
        try {
          Thread.sleep((int)(1.05*(11-player.curSpeed)));
        } catch(InterruptedException e) {
          e.printStackTrace();
        }
      }
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

    graphicUI.drawUI(g2d);
    
    g2d.dispose();
  }
}
