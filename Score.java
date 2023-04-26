
public class Score {
    private static int maxScore;
    private static int curScore;

    protected static void addScore(int score){
        curScore += score;
        if (maxScore < curScore) {
            maxScore = curScore;
            Scoreboard.getProfile(GamePanel.playerName).setHighScore(maxScore);
            if(Scoreboard.getToBeat(GamePanel.playerName) != null 
            && maxScore > Scoreboard.getToBeat(GamePanel.playerName).getHighScore()) {
                Scoreboard.overtake(GamePanel.playerName);
            }
        }
    }

    protected static void restart() {
        maxScore = Scoreboard.getProfile(GamePanel.playerName).getHighScore();
        curScore = 0;
    }
    protected static void reset() {
        maxScore = 0;
        curScore = 0;
    }

    protected static int getCurScore() {
        return curScore;
    }

    protected static String MAX_SCORE() {
        return String.format("[High Score : %d]", maxScore);
    }

    protected static String CUR_SCORE() {
        return String.format("[Score : %d]", curScore);
    }

    @Override
    public String toString() {
        return String.format("[maxScore : %d, curScore : %d]",maxScore, curScore);
    }
}
