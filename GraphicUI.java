import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.plaf.FontUIResource;

public class GraphicUI {
  private GamePanel gp;
  private Font titleFont;
  private Font normalFont;

  private BufferedImage gridmap;

  public GraphicUI(GamePanel gp) throws IOException {
    this.gp = gp;
    titleFont = new FontUIResource("Daydream", 0, 50);
    normalFont = new FontUIResource("W95FA", 0, 30);
    gridmap = ImageIO.read(new File("sprites/background/gridmap.png"));
  }
  
  public void drawUI(Graphics2D g2d) {
    switch(GamePanel.state) {
      case MENU:
        g2d.setColor(Color.ORANGE);
        g2d.setFont(titleFont);
        g2d.drawString("Snake", getCenteredX("Snake", g2d), (Main.height/2)-80);
        g2d.drawString("Jungle", getCenteredX("Jungle", g2d), Main.height/2);
        g2d.setFont(normalFont);
        g2d.drawString("press ENTER to play!"
          ,getCenteredX("press ENTER to play!", g2d), (Main.height/2)+200);
        break;
      case PLAYZONE:
        g2d.drawImage(gridmap,0,0,Main.width,Main.height,null);
        if(gp.player != null) {
          g2d.drawImage(gp.player.headSprite[gp.player.facing], gp.player.x-8, gp.player.y-8, Main.width/64, Main.height/36, null);
          // TO-DO draw whole body
        }
        g2d.setColor(Color.WHITE);
        g2d.drawString(gp.player.toString(), 500, 500);
        break;
    }
    g2d.drawString(String.format("res:%dx%d",Main.width,Main.height), 400, 500);
  }

  /**
   * Get {@code x} position for a centered text
   * @param text
   * @param g2d
   * @return Integer of {@code x} to set position of text to paint in the center
   */
  private static int getCenteredX(String text, Graphics2D g2d) {
    int length = (int)g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
    return Main.width/2 - length/2;
  }
}
