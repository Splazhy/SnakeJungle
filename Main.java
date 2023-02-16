import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;

public class Main extends JFrame {
  protected GamePanel gamePanel;

  public Main() throws IOException {

    gamePanel = new GamePanel();

    this.add(gamePanel);
    this.setTitle("Snake Jungle (prototype)");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setForeground(Color.WHITE);
    this.setBackground(Color.WHITE);
    this.pack();
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    this.setVisible(true);
    
    gamePanel.start();
  }

  public static void main(String[] args) throws IOException {
    new Main();
  }
}
