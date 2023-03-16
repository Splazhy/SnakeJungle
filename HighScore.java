import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class HighScore {

    private static String fileName = "highscore.txt";
    private static File file = new File(fileName);

    public static int getHighScore() {
        String highScore = "0";
        Scanner sc = null;
        try {
            if (file.exists()) {
                sc = new Scanner(file);
                if (sc.hasNextLine()) {
                    highScore = sc.nextLine();
                }
                sc.close();
            } else {
                setHighScore(highScore);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (sc != null) {
                sc.close();
            }
        }
        return Integer.valueOf(highScore);
    }

    public static void setHighScore(String highScore) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(Integer.parseInt(highScore));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
