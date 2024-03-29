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
    if(pressed == KeyEvent.VK_W) {
      movementIsHeld[0] = true;
    }
    if(pressed == KeyEvent.VK_A) {
      movementIsHeld[1] = true;
    }
    if(pressed == KeyEvent.VK_S) {
      movementIsHeld[2] = true;
    }
    if(pressed == KeyEvent.VK_D) {
      movementIsHeld[3] = true;
    }

    /* CHEAT MODE */
    // switch(GamePanel.state) {
    // case MENU: break;
    // case LOADING: break;
    // case PLAYZONE:
    //   if(pressed == KeyEvent.VK_SEMICOLON) { // debug
    //     gp.player.grow(5);
    //   }
    //   if(pressed == KeyEvent.VK_SLASH) {
    //     Score.addScore(5);
    //   }
    //   break;
    // case PAUSE: break;
    // case GAMEOVER: break;
    // }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    released = e.getExtendedKeyCode();
    // System.out.println("released: " + released);
    switch(GamePanel.state) {
    case MENU:
      if(released == KeyEvent.VK_ENTER) {
        if(gp.nameField.getText().matches("^[a-zA-Z0-9]{2,15}$"))
          GamePanel.state = State.LOADING;
      }
      break;
    case LOADING: break;
    case PAUSE:
      if(released == KeyEvent.VK_ESCAPE) {
        GamePanel.state = State.PLAYZONE;
      } else if(released == KeyEvent.VK_ENTER) {
        gp.unload();
      }
      break;
    case PLAYZONE:
      if(released == KeyEvent.VK_ESCAPE) {
        GamePanel.state = State.PAUSE;
      }
      break;
    case GAMEOVER:
      if(released == KeyEvent.VK_ESCAPE) {
        gp.unload();
      } else if(released == KeyEvent.VK_ENTER) {
        gp.load();
      }
    } // end of switch
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
    if(released == KeyEvent.VK_F3) {
      GamePanel.isDebugging = !GamePanel.isDebugging;
    }
  }
}
