public class Apple extends Score {
    protected Score aScore;

    public Apple () {
        aScore.score = 10;
    }
    public void spawnApple() {
        
    }

    public void destroid() {
        aScore.increaseScore(aScore.score);
    }
}
