import javax.swing.JFrame;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Main extends JFrame {
  protected static int width;
  protected static int height;
  private GamePanel gamePanel;

  public Main() {

    gamePanel = new GamePanel();
    add(gamePanel);
    
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        width = getSize().width;
        height = getSize().height;
        if(GamePanel.state == State.PLAYZONE || GamePanel.state == State.PAUSE) {
          GamePanel.state = State.PAUSE;
        }
        gamePanel.updatePanel();
      }
    });
    setTitle("Snake Jungle (alpha)");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setResizable(true);
    setVisible(true);
    
    gamePanel.start();
  }

  public static void main(String[] args) {
    new Main();
    System.out.println(System.getProperty("os.name"));
  }
}
