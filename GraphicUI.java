import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.plaf.ColorUIResource;

public class GraphicUI {
  private GamePanel gp;
  private Font titleFont;
  private Font normalFont;

  public GraphicUI(GamePanel gp) {
    this.gp = gp;
    try {
      titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/DayDream.ttf")).deriveFont(50f);
      normalFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/W95FA.otf")).deriveFont(30f);
    } catch(IOException e) {
      e.printStackTrace();
    } catch (FontFormatException e) {
      e.printStackTrace();
    }
  }
  
  protected void drawUI(Graphics2D g2d) {
    switch(GamePanel.state) {
    case MENU:
      g2d.setColor(Color.ORANGE);
      g2d.setFont(titleFont);
      g2d.drawString("Snake", getCenteredX("Snake", g2d), (Main.height/2)-80);
      g2d.drawString("Jungle", getCenteredX("Jungle", g2d), Main.height/2);
      g2d.setFont(normalFont);
      g2d.drawString("press ENTER to play!"
      ,getCenteredX("press ENTER to play!", g2d), Main.height-(Main.height/4));
      break;

    case LOADING:
      g2d.setColor(Color.WHITE);
      g2d.setFont(normalFont);
      g2d.drawString("Loading...", getCenteredX("Loading", g2d), Main.height/2);
      break;

    case PAUSE:
      g2d.setColor(Color.WHITE);
      g2d.setFont(normalFont);
      g2d.drawString("PAUSED", 40, Main.height/8);
      g2d.drawString("[ESC] continue game", 40, Main.height/8+40);
      g2d.drawString("[ENTER] to title screen", 40, Main.height/8+80);
      break;

    case PLAYZONE:
      g2d.setColor(Color.WHITE);
      if(gp.player != null) {
        g2d.drawString(Arrays.toString(gp.player.headCellPos),100, 150); // debug
        g2d.drawString(gp.player.toString(), 100, 200); // debug
        g2d.drawString(gp.player.facingQ.toString(), 300, 200); // debug
        g2d.drawString(Arrays.toString(BotHungrySnake.markCellPos),100, 300);
        g2d.drawString(Arrays.toString(BotHungrySnake.targetedFood),100, 400);
        g2d.setFont(normalFont);

        // draws gameplay UI here
        g2d.drawString(Score.MAX_SCORE(), 100, 280);
        g2d.drawString(Score.CUR_SCORE(), 100, 320);

      }
      g2d.drawString(Arrays.toString(GridMap.offset),100, 100); // debug
      break;
    
    case GAMEOVER:
      g2d.setFont(normalFont);
      g2d.setColor(new ColorUIResource(190, 68, 55));
      g2d.drawString("GAME OVER", getCenteredX("GAME OVER", g2d), Main.height/2);
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
