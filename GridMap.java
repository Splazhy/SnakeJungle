import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GridMap {
  protected final static int GRID_PIXELS = 640;
  protected final static int CELL_PER_ROW = 40;
  protected static int size;
  /**
   * top-left offset for JFrame
   */
  protected static int[] offset;
  /**
   * จุดซ้ายบนของแต่ละช่องใน GridMap 
   */
  protected static int[][][] cellLayout; // [x][y]{x,y}
  private BufferedImage img;

  protected GridMap() {
    offset = new int[2];
    cellLayout = new int[CELL_PER_ROW][CELL_PER_ROW][2];
    update();
    for(int x = 0; x < CELL_PER_ROW; x++) {
      for(int y = 0; y < CELL_PER_ROW; y++) {
        cellLayout[x][y][0] = x*16;
        cellLayout[x][y][1] = y*16;
      }
    }
    try {
      img = ImageIO.read(new File("sprites/background/gridmap.png"));
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  protected void draw(Graphics2D g2d) {
    g2d.drawImage(img, 0, 0, null);
  }

  protected void update() {
    size = Math.min(Main.width-Main.width/3, Main.height-Main.height/8);
    offset[0] = Main.width/2 - size/2;
    offset[1] = Main.height/2 - size/2;
  }

  /**
   * @param x
   * @param y
   * @return positions according to gridimage and gridmap
   */
  protected static int[] getCellPos(int x, int y) {
    int cellX, cellY;
    {
      int lowerX = (x / 16) * 16;
      int upperX = lowerX + 16;
      int lowerY = (y / 16) * 16;
      int upperY = lowerY + 16;

      cellX = ((x - lowerX < upperX - x) ? lowerX/16 : upperX/16) % 40;
      cellY = ((y - lowerY < upperY - y) ? lowerY/16 : upperY/16) % 40;
    }
    return new int[] {cellX, cellY};
  }
}
