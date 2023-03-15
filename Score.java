
public class Score {
    private static int maxScore = 0; // test 0 score
    private static int curScore = 0;

    protected static void addScore(int score){
        curScore += score;
        if (maxScore < curScore) {
            maxScore = curScore;
        }
    }

    protected static void restart() {
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
