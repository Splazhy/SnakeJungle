
public class Score {
    private static int maxScore = HighScore.getHighScore();
    private static int curScore = 0;

    public static void addScore(int score){
        curScore += score;
        if (maxScore < curScore) {
            maxScore = curScore;
        }
    }

    public static void reScore(int score){
        if (maxScore > HighScore.getHighScore()) HighScore.setHighScore(String.valueOf(maxScore));
        curScore = 0;
    }

    public static String MAX_SCORE() {
        return String.format("[High Score : %d]", maxScore);
    }

    public static String CUR_SCORE() {
        return String.format("[Score : %d]", curScore);
    }

    @Override
    public String toString() {
        return String.format("[maxScore : %d, curScore : %d]",maxScore, curScore);
    }
}
