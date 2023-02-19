
public class Score {
    public int score;


    public Score() {
        score = 0;
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
