import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
  private GamePanel gp;
  /**
   * An array keeping track of WASD keys.
   * {@code true} if the key is held;
   * {@code false} otherwise.
   * <p> [0] : W [1] : A [2] : S [3] : D
   */
  protected boolean[] movementIsHeld;
  private int pressed;
  private int released;

  public KeyHandler(GamePanel gp) {
    this.gp = gp;
    movementIsHeld = new boolean[4];
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // unused
  }

  @Override
  public void keyPressed(KeyEvent e) {
    pressed = e.getExtendedKeyCode();
    // System.out.println("pressed: " + pressed);
    if(GamePanel.state == State.PLAYZONE && gp.player != null) {
      if(pressed == KeyEvent.VK_W && gp.player.facing != 2) {
        movementIsHeld[0] = true;
      }
      if(pressed == KeyEvent.VK_A && gp.player.facing != 3) {
        movementIsHeld[1] = true;
      }
      if(pressed == KeyEvent.VK_S && gp.player.facing != 0) {
        movementIsHeld[2] = true;
      }
      if(pressed == KeyEvent.VK_D && gp.player.facing != 1) {
        movementIsHeld[3] = true;
      }
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    released = e.getExtendedKeyCode();
    // System.out.println("released: " + released);
    switch(GamePanel.state) {
    case MENU:
      if(released == KeyEvent.VK_ENTER) {
        GamePanel.state = State.PLAYZONE;
        gp.load();
      }
      break;
    case PAUSE:
      if(released == KeyEvent.VK_ESCAPE) {
        GamePanel.state = State.PLAYZONE;
      } else if(released == KeyEvent.VK_ENTER) {
        GamePanel.state = State.MENU;
        gp.unload();
      }
      break;
    case PLAYZONE:
      if(released == KeyEvent.VK_ESCAPE) {
        GamePanel.state = State.PAUSE;
      }
      if(released == KeyEvent.VK_W) {
        movementIsHeld[0] = false;
      }
      if(released == KeyEvent.VK_A) {
        movementIsHeld[1] = false;
      }
      if(released == KeyEvent.VK_S) {
        movementIsHeld[2] = false;
      }
      if(released == KeyEvent.VK_D) {
        movementIsHeld[3] = false;
      }
      break;
    }
  }
}
