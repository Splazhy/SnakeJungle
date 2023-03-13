import java.awt.geom.Ellipse2D;

public class GameHitbox extends Ellipse2D.Double {
  protected final int ID;

  protected GameHitbox(double x, double y, double w, double h, int ID) {
    super(x, y, w, h);
    this.ID = ID;
  }
}
