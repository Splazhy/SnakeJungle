public class Profile implements Comparable<Profile> {
    private String name;
    private String actual;
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
