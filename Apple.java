
import java.util.*;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Apple{
    protected static int appleX,appleY;
    protected static int newAppleX,newAppleY;
    private BufferedImage appleImg;
    private static Random random;
    private static GameHitbox appleHitbox;

    public Apple(GridMap gridmap){
        random = new Random();
        try {
            appleImg = ImageIO.read(new File("sprites/food/apple.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
        appleHitbox = new GameHitbox(appleX+4,appleY+4,6,6, -1); //ID(-1)
        GamePanel.hitboxList.add(appleHitbox);

        // respawnApple();
        
    }

    public static void respawnApple(){
        newAppleX = random.nextInt(40);
        newAppleY = random.nextInt(40);
    }

    
    protected void draw(Graphics2D g2d) {
      g2d.drawImage(appleImg, appleX, appleY, null);

    }

    protected void tick(){
        // respawnApple();
        appleX = GridMap.cellLayout[newAppleX][newAppleY][0];
        appleY = GridMap.cellLayout[newAppleX][newAppleY][1];

        // appleX = GridMap.cellLayout[10][10][0];
        // appleY = GridMap.cellLayout[10][10][1];
        appleHitbox.setFrame(appleX+2, appleY+2, 10, 10);
        
    }



}