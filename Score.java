
public class Score {
    public int score = 0; // test 0 score


    public Score() {
        /* init score */
        score = 10;
    }

    public void increaseScore(int score){
        if (score > 0)
        this.score += score;
    }

    public void decreaseScore(int score){
        if (this.score >= 0){
            this.score -= score;
        }
        if (this.score < 0) {
            this.score = 0;
        }
    }

    @Override
    public String toString() {
        return String.format("[Score : %d]",score);
    }
}
