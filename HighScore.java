import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class HighScore {

    private static File file = new File("highscore.txt");

    public static int getHighScore() {
        int highScore = 0;
        try {
            file.createNewFile();
            Scanner sc = new Scanner(file);
            if (sc.hasNextLine()) {
                highScore = Integer.parseInt(sc.nextLine());
            } else {
                setHighScore(0);
            }
            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return highScore;
    }

    public static void setHighScore(int highScore) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(Integer.toString(highScore));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
