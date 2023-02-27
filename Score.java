
public class Score {
    private static int maxScore = 0; // test 0 score
    private static int curScore = 0;

    public static void inScore(int score){
        curScore += score;
        if (maxScore < curScore) {
            maxScore = curScore;
        }
    }

    public static void inScore(){
        curScore += 50;
        if (maxScore < curScore) {
            maxScore = curScore;
        }
    }

    public static void reScore(int score){
        curScore = 0;
    }

    public static String MAX_SCORE() {
        return String.format("[maxScore : %d]", maxScore);
    }

    public static String CUR_SCORE() {
        return String.format("[maxScore : %d]", curScore);
    }

    @Override
    public String toString() {
        return String.format("[maxScore : %d, curScore : %d]",maxScore, curScore);
    }
}
