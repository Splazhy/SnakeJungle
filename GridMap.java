import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class GridMap {
  protected final static int length = 40;
  /**
   * [0]: x position, [1]: y position, [3]: square side length
   */
  protected int[] offset;
  protected static int[][][] cellLayout; // [y][x]{x,y}
  protected BufferedImage img;

  protected GridMap() {
    offset = new int[3];
    cellLayout = new int[length][length][2];
    update();
    try {
      img = ImageIO.read(new File("sprites/background/gridmap.png"));
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * need optimizing
   */
  protected void update() {
    offset[2] = Math.min(Main.width-Main.width/3, Main.height-Main.height/8);
    offset[0] = Main.width/2 - offset[2]/2;
    offset[1] = Main.height/2 - offset[2]/2;
    
    int cellLeft = 40;
    int areaTaken = 0;
    double area = offset[2];
    int[] cellArea = new int[40];
    
    for(int i = 0; i < cellArea.length; i++) {
      cellArea[i] = (int)Math.round((area - areaTaken) / cellLeft);
      areaTaken += cellArea[i];
      cellLeft--;
    }

    int a = cellArea[0];
    int b = 0;
    int bCnt = 0;
    for(int i = 1; i < cellArea.length; i++) {
      if(cellArea[i] != a) {
        b = cellArea[i];
        bCnt++;
      }
    }
    if(b == 0)
      return;
    double bRatio = (double)length/bCnt;
    int idxToFill = 0;
    for(int i = 1; i <= bCnt; i++) {
      while(idxToFill < Math.round(bRatio*i)-1) {
        cellArea[idxToFill] = a;
        idxToFill++;
      }
      cellArea[(int)Math.round(bRatio*i)-1] = b;
      idxToFill++;
    }

    int posY = offset[1];
    for(int i = 0; i < cellLayout.length; i++) {
      int posX = offset[0];
      for(int j = 0; j < cellLayout[i].length; j++) {
        cellLayout[i][j][0] = posX;
        cellLayout[i][j][1] = posY;
        posX += cellArea[j];
      }
      posY += cellArea[i];
      cellLeft--;
    }
    System.out.println(Arrays.toString(cellArea)); // debug
  }

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
    return new int[][] {cellLayout[cellY][cellX], {cellX, cellY}};
  }
}
