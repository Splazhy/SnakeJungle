import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GridMap {
  protected final static int widthCells = 40;
  protected final static int heightCells = 40;
  protected BufferedImage img;

  protected GridMap() {
    try {
      img = ImageIO.read(new File("sprites/background/gridmap.png"));
    } catch(IOException e) {
      e.printStackTrace();
    }
  }
}
