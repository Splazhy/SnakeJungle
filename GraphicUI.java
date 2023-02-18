import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.plaf.FontUIResource;

public class GraphicUI {
  private GamePanel gp;
  private Font titleFont;
  private Font normalFont;

  public GraphicUI(GamePanel gp) throws IOException {
    this.gp = gp;
    titleFont = new FontUIResource("Daydream", 0, 50);
    normalFont = new FontUIResource("W95FA", 0, 30);
  }
  
  public void drawUI(Graphics2D g2d) {
    switch(GamePanel.state) {
    case MENU:
      g2d.setColor(Color.ORANGE);
      g2d.setFont(titleFont);
      g2d.drawString("Snake", getCenteredX("Snake", g2d), (Main.height/2)-80);
      g2d.drawString("Jungle", getCenteredX("Jungle", g2d), Main.height/2);
      g2d.setFont(normalFont);
      g2d.drawString("press ↵ to play!"
      ,getCenteredX("press ↵ to play!", g2d), Main.height-(Main.height/4));
      break;

    case PAUSE:
      if(Main.isUpdatingFrameSize) {
        if(gp.player != null) {
          gp.gridMap.update();
          gp.player.calibratePosition();
        }
        Main.isUpdatingFrameSize = false;
      }
      g2d.setColor(Color.WHITE);
      g2d.setFont(normalFont);
      g2d.drawString("PAUSED", 40, Main.height/8);
      g2d.drawString("[esc] continue game", 40, Main.height/8+40);
      g2d.drawString("[↵] exit to title screen", 40, Main.height/8+80);
      break;

    case PLAYZONE:
      g2d.setColor(Color.WHITE);
      if(gp.player != null) {
        g2d.drawString(Arrays.toString(gp.player.cellPos[1]),100, 150); // debug
        g2d.drawString(gp.player.toString(), 100, 200); // debug
        // draws gameplay UI here
      }
      g2d.drawString(Arrays.toString(GridMap.offset),100, 100); // debug
      break;
    }
    g2d.drawString(String.format("res:%dx%d",Main.width,Main.height), 100, 50); // debugging
  }

  /**
   * Get {@code x} position for a centered text
   * @param text
   * @param g2d
   * @return Integer of {@code x} to set position of text to paint in the center
   */
  private static int getCenteredX(String text, Graphics2D g2d) {
    int numOfGrid = (int)g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
    return Main.width/2 - numOfGrid/2;
  }
}
