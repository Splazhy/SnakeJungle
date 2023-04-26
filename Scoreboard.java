import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Scoreboard {
    private static HashMap<String, Profile> scoreboard;
    private static ArrayList<Profile> leaderboard;

    public Scoreboard(String filepath) {
        scoreboard = new HashMap<>();
        leaderboard = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File(filepath));
            String[] line;
            while(sc.hasNextLine()) {
                line = sc.nextLine().split(",");
                scoreboard.put(line[0], new Profile(line[0], line[1], line[2]));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        leaderboard.addAll(scoreboard.values());
        Collections.sort(leaderboard);
    }

    public static void overtake(String self) {
        int idx = leaderboard.indexOf(scoreboard.get(self));
        Collections.swap(leaderboard, idx-1, idx);
    }

    public static Profile getProfile(String name) {
        return scoreboard.get(name.toLowerCase());
    }

    public static Profile getToBeat(String self) {
        int idx = leaderboard.indexOf(scoreboard.get(self));
        return (idx != 0) ? leaderboard.get(idx-1) : null;
    }

    public boolean hasName(String name) {
        return scoreboard.containsKey(name.toLowerCase());
    }

    public void addNewProfile(String name) {
        Profile profile = new Profile(name.toLowerCase(), name, "0");
        scoreboard.put(name.toLowerCase(), profile);
        leaderboard.add(profile);
    }

    public void save(String filepath) {
        try {
            FileWriter writer = new FileWriter(filepath, false);
            for(Profile p : leaderboard) {
                writer.write(String.format("%s,%s,%d\n"
                ,p.getName(),p.getActualName(),p.getHighScore()));
            }
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void drawLeaderBoard(Graphics2D g2d) {
        g2d.setColor(Color.white);
        g2d.setFont(GraphicUI.normalFont.deriveFont(25f));
        g2d.drawString("Leaderboard",(int)(Main.width*0.85)+20, 40);
        for(int i = 0, rowPos = 70; i < 20; i++, rowPos+=30) {
            if(i >= leaderboard.size())
                break;
            g2d.drawString(leaderboard.get(i).toString(), (int)(Main.width*0.85), rowPos);
        }
    }
}
