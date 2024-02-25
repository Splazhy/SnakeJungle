import java.util.Timer;
import java.util.TimerTask;

public class GameoverTimer {
    private GamePanel gp;

    private Timer timer;
    private int timeLeft;
    private int seconds;

    public GameoverTimer(GamePanel gp, int seconds) {
        this.gp = gp;
        timeLeft = seconds;
        this.seconds = seconds;
        timer = new Timer();
    }

    public void start() {
        timer.schedule(new TimerTask() {
            public void run() {
                timeLeft--;
                if (timeLeft == 0) {
                    gp.unload();
                    reset();
                }
            }
        }, 0, 1000);
    }

    public void reset() {
        timer.cancel();
        timeLeft = seconds;
        timer = new Timer();
    }

    @Override
    public String toString() {
        return Integer.toString(timeLeft);
    }
}
