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

  private GridMap gridMap;
  private int[] gridMapOffset;

  public GraphicUI(GamePanel gp) throws IOException {
    this.gp = gp;
    titleFont = new FontUIResource("Daydream", 0, 50);
    normalFont = new FontUIResource("W95FA", 0, 30);
    gridMap = new GridMap();
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
        if(Main.isUpdatingFrameSize) {
          gridMapOffset = getGridMapOffset();

          Main.isUpdatingFrameSize = false;
        }
        g2d.setColor(Color.WHITE);
        g2d.drawImage(gridMap.img, gridMapOffset[0], gridMapOffset[1]
        ,gridMapOffset[2], gridMapOffset[2], null);
        g2d.drawString(Arrays.toString(gridMapOffset),100,100);
        if(gp.player != null) {
          g2d.drawString(gp.player.toString(), 500, 500); // debug
          g2d.drawImage(gp.player.headSprite[gp.player.facing]
          ,gp.player.x-8, gp.player.y-8
          ,Main.width/32, Main.height/32, null);
          // TO-DO draw whole body (for loop maybe)
        }
        break;
    }
    g2d.drawString(String.format("res:%dx%d",Main.width,Main.height), 400, 500); // debugging
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

  /**
   * 
   * @return [0] x position, [1] y position, [2] square size
   */
  private static int[] getGridMapOffset() {
    int[] arr = new int[3];
    arr[2] = Math.min(Main.width-Main.width/3, Main.height-Main.height/8);
    arr[0] = Main.width/2 - arr[2]/2;
    arr[1] = Main.height/2 - arr[2]/2;
    return arr;
  }
}
