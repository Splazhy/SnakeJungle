import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.plaf.ColorUIResource;

public class GraphicUI {
  private GamePanel gp;
  public static Font titleFont;
  public static Font normalFont;

  private GameoverTimer timer;

  private BufferedImage[] audioIcon;

  public GraphicUI(GamePanel gp, GameoverTimer timer) {
    this.gp = gp;
    audioIcon = new BufferedImage[4];
    this.timer = timer;
    try {
      titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/DayDream.ttf")).deriveFont(50f);
      normalFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/W95FA.otf")).deriveFont(30f);
      audioIcon[0] = ImageIO.read(new File("sprites/icon/audio_mute.png"));
      audioIcon[1] = ImageIO.read(new File("sprites/icon/audio_low.png"));
      audioIcon[2] = ImageIO.read(new File("sprites/icon/audio_medium.png"));
      audioIcon[3] = ImageIO.read(new File("sprites/icon/audio_high.png"));
    } catch(IOException e) {
      e.printStackTrace();
    } catch (FontFormatException e) {
      e.printStackTrace();
    }
  }
  
  protected void drawUI(Graphics2D g2d) {
    int debugOffsetX = (int)((Main.width/2) + (Main.width-Main.width/3)/2);
    switch(GamePanel.state) {
    case MENU:
      g2d.setColor(Color.ORANGE);
      g2d.setFont(titleFont);
      g2d.drawString("Snake", getCenteredX("Snake", g2d), (Main.height/2)-80);
      g2d.drawString("Jungle", getCenteredX("Jungle", g2d), Main.height/2);
      g2d.setFont(normalFont);
      g2d.drawString("press ENTER to play!"
      ,getCenteredX("press ENTER to play!", g2d), Main.height-(Main.height/4));
      g2d.drawImage(audioIcon[gp.audioIconIdx], Main.width/17, (int)(Main.height/1.12d), Main.width/30, (Main.width/30 * 24)/31, null);
      g2d.setColor(Color.white);
      g2d.drawString("Your name:", gp.nameField.getX()-140, gp.nameField.getY()+gp.nameField.getFont().getSize()+4);
      break;

    case LOADING:
      g2d.setColor(Color.WHITE);
      g2d.setFont(normalFont.deriveFont(80f));
      g2d.drawString("Loading...", getCenteredX("Loading", g2d), Main.height/2);
      break;

    case PAUSE:
      g2d.setColor(Color.WHITE);
      g2d.setFont(normalFont);
      g2d.drawString("PAUSED", 40, Main.height/8);
      g2d.drawString("[ESC] continue game", 40, Main.height/8+40);
      g2d.drawString("[ENTER] to title screen", 40, Main.height/8+80);
      g2d.drawString("Control", Main.width/40, 570);
      g2d.drawString("[W, A, S, D]", Main.width/40, 600);
      break;

    case GAMEOVER:
      g2d.setColor(new Color(0, 0, 0, 100));
      g2d.fillRect(GridMap.offset[0], GridMap.offset[1], GridMap.size, GridMap.size);
      g2d.setFont(normalFont.deriveFont(Main.width/18f));
      g2d.setColor(new ColorUIResource(190, 68, 55));
      g2d.drawString("GAME OVER", getCenteredX("GAME OVER", g2d), Main.height/2 - 80);
      g2d.setFont(normalFont.deriveFont(70f));
      g2d.drawString(timer.toString(), getCenteredX(timer.toString(), g2d), Main.height/2+30);
      g2d.setFont(normalFont);
      g2d.drawString("Continue?", getCenteredX("Continue?", g2d), Main.height/2+100);
      g2d.drawString("press [Enter]", getCenteredX("press [Enter]", g2d), Main.height/2 + 140);

    case PLAYZONE:
      g2d.setColor(Color.WHITE);
      g2d.setFont(normalFont.deriveFont(Main.width/48.0f));

      if(GamePanel.isDebugging) {
        String facingStr = (gp.player.facing == 0) ? "up"
          : (gp.player.facing == 1) ? "left"
          : (gp.player.facing == 2) ? "down" : "right";
        g2d.drawString("facing: " + facingStr, debugOffsetX, 100); // debug
        g2d.drawString("length: " + Integer.toString(gp.player.partList.size()), debugOffsetX, 150);
        g2d.drawString(Arrays.toString(gp.player.headCellPos),debugOffsetX, 200); // debug
        g2d.drawString(gp.player.toString(), debugOffsetX, 250); // debug
      }

      // draws gameplay UI here
      g2d.drawString(Score.MAX_SCORE(), Main.width/40, 280);
      g2d.drawString(Score.CUR_SCORE(), Main.width/40, 320);
      g2d.drawString("Control", Main.width/40, 570);
      g2d.drawString("[W, A, S, D]", Main.width/40, 600);

      break;
    }
    g2d.setFont(normalFont.deriveFont(Main.width/48f));
    g2d.setColor(Color.WHITE);
    if(GamePanel.isDebugging) {
      g2d.drawString(String.format("res: %dx%d",Main.width,Main.height), debugOffsetX, 50); // debugging
    }
  }

  /**
   * Get {@code x} position for a centered text
   * @param text
   * @param g2d
   * @return Integer of {@code x} to set position of text to paint in the center
   */
  private static int getCenteredX(String text, Graphics2D g2d) {
    int stringBoundWidth = (int)g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
    return Main.width/2 - stringBoundWidth/2;
  }
}
