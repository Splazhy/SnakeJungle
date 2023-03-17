/**
 * Define state of this program
*/
public enum State {
  MENU(0),
  LOADING(0),
  PLAYZONE(1),
  PAUSE(1),
  GAMEOVER(1);
  // SETTINGS

  public final int screen;

  private State(int screen) {
    this.screen = screen;
  }
}
