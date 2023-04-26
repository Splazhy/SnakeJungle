public class Profile implements Comparable<Profile> {
    private String name;
    private String actual;
    private int placement;
    private int highscore;

    public Profile(String name, String actual, String highscore) {
        this.name = name;
        this.actual = actual;
        this.highscore = Integer.parseInt(highscore);
    }

    public String getName() {
        return name;
    }

    public String getActualName() {
        return actual;
    }

    public int getPlacement() {
        return placement;
    }

    public void setPlacement(int placement) {
        this.placement = placement;
    }

    public int getHighScore() {
        return highscore;
    }

    protected void setHighScore(int highscore) {
        this.highscore = highscore;
    }

    @Override
    public int compareTo(Profile b) {
        return b.highscore - highscore;
    }

    @Override
    public String toString() {
        return String.format("%s: %,d pts",actual,highscore);
    }
}
