import java.io.IOException;
import javax.swing.JFrame;
import java.awt.event.*;

public class Main extends JFrame {
  protected static int width;
  protected static int height;
  protected GamePanel gamePanel;

  public Main() throws IOException {

    gamePanel = new GamePanel();
    
    add(gamePanel);
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        width = getSize().width;
        height = getSize().height;
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
  }
}
