import java.awt.geom.Ellipse2D;

public class GameHitbox extends Ellipse2D.Double {
  protected final int ID;
  protected final Object owner;

  protected GameHitbox(double x, double y, double w, double h, int ID, Object owner) {
    super(x, y, w, h);
    this.ID = ID;
    this.owner = owner;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + owner.hashCode();
    return result;
  }
}
