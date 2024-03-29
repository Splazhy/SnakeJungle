import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
  protected static HashMap<Integer, HashSet<Integer>> cellDetails;
  protected static ArrayList<Integer> freeCells;
  private BufferedImage img;

  protected GridMap() {
    offset = new int[2];
    cellLayout = new int[CELL_PER_ROW][CELL_PER_ROW][2];
    cellDetails = new HashMap<>();
    freeCells = new ArrayList<>();
    update();
    for(int x = 0; x < CELL_PER_ROW; x++) {
      for(int y = 0; y < CELL_PER_ROW; y++) {
        cellLayout[x][y][0] = x*16;
        cellLayout[x][y][1] = y*16;
        cellDetails.put(x*100 + y, new HashSet<>());
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

  protected static void updateFreeCells() {
    freeCells.clear();
    cellDetails.forEach((k,v) -> {
      if(v.isEmpty()) freeCells.add(k);
    });
  }
}
