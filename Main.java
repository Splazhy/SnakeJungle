import java.io.IOException;
import javax.swing.JFrame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Main extends JFrame {
  protected static int width;
  protected static int height;
  private GamePanel gamePanel;

  public Main() throws IOException {

    gamePanel = new GamePanel();
    add(gamePanel);
    
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        width = getSize().width;
        height = getSize().height;
        if(GamePanel.state == STATE.PLAYZONE)
          GamePanel.state = STATE.PAUSE;
        gamePanel.updatePanel();
      }
    });
    setTitle("Snake Jungle (prototype)");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setResizable(true);
    setVisible(true);
    
    gamePanel.start();
  }

  public static void main(String[] args) throws IOException {
    new Main();
    System.out.println(System.getProperty("os.name"));
  }
}
