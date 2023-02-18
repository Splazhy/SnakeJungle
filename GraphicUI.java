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
          gridMap.update();
          Main.isUpdatingFrameSize = false;
        }
        g2d.setColor(Color.WHITE);
        g2d.drawImage(gridMap.img, gridMap.offset[0], gridMap.offset[1]
        ,gridMap.offset[2], gridMap.offset[2], null);
        g2d.drawString(Arrays.toString(gridMap.offset),100,100); // debug
        if(gp.player != null) {
          g2d.drawString(Arrays.toString(gp.player.cellPos),100,150); // debug
          g2d.drawString(gp.player.toString(), 500, 500); // debug
          g2d.drawImage(gp.player.headSprite[gp.player.facing]
          ,gp.player.x, gp.player.y
          ,gridMap.offset[2]/GridMap.cellsLength
          ,gridMap.offset[2]/GridMap.cellsLength, null);
          // TO-DO draw whole body (for loop maybe)
        }
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
    int length = (int)g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
    return Main.width/2 - length/2;
  }
}
