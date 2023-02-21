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
   * top-left offset
   */
  protected static int[] offset;
  protected static int[][][] cellLayout; // [y][x]{x,y}
  private BufferedImage img;

  protected GridMap() {
    offset = new int[2];
    cellLayout = new int[CELL_PER_ROW][CELL_PER_ROW][2];
    update();
    for(int i = 0; i < CELL_PER_ROW; i++) {
      for(int j = 0; j < CELL_PER_ROW; j++) {
        cellLayout[i][j][0] = j*16;
        cellLayout[i][j][1] = i*16;
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

  /**
   * What this does: <p>
   * 1. recalculate size and offset <p>
   * 2. get pixel inequality <p>
   * 3. spread inequality across cellArea array <p>
   * 4. compute cellLayout with cellArea and offset <p>
   */
  protected void update() {
    size = Math.min(Main.width-Main.width/3, Main.height-Main.height/8);
    offset[0] = Main.width/2 - size/2;
    offset[1] = Main.height/2 - size/2;
    
    // System.out.println(Arrays.toString(cellArea)); // debug
  }

  /**
   * FOR SNAKE OBJECT ONLY
   * @param x
   * @param y
   * @return snake postions according frame and gridmap
   */
  protected static int[][] getCellPos(int x, int y) {
    int cellX = -1, cellY = -1;
    int markXMinDist = Integer.MAX_VALUE;
    int markYMinDist = Integer.MAX_VALUE;

    for(int i = 0; i < cellLayout.length; i++) {
      if(Math.abs(x-cellLayout[0][i][0]) < markXMinDist) {
        markXMinDist = Math.abs(x-cellLayout[0][i][0]);
        cellX = i;
      }
      if(Math.abs(y-cellLayout[i][0][1]) < markYMinDist) {
        markYMinDist = Math.abs(y-cellLayout[i][0][1]);
        cellY = i;
      }
    }
    if(cellX == 0) {
      cellX = (Math.abs(x-cellLayout[0][0][0])
        > Math.abs(x-(cellLayout[0][0][0]-16))) ? 39 : 0;
    } else if(cellX == CELL_PER_ROW-1) {
      cellX = (Math.abs(x-cellLayout[0][39][0])
        > Math.abs(x-(cellLayout[0][39][0]+16))) ? 0 : 39;
    }
    if(cellY == 0) {
      cellY = (Math.abs(y-cellLayout[0][0][1])
        > Math.abs(y-(cellLayout[0][0][1]-16))) ? 39 : 0;
    } else if(cellY == CELL_PER_ROW-1) {
      cellY = (Math.abs(y-cellLayout[39][0][1])
        > Math.abs(y-(cellLayout[39][0][1]+16))) ? 0 : 39;
    }
    return new int[][] {cellLayout[cellY][cellX], {cellX, cellY}};
  }
}
