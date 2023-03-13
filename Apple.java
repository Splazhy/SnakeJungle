
import java.util.*;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Apple{
    protected int appleX,appleY;
    protected int newAppleX,newAppleY;
    private BufferedImage appleImg;
    private Random random;

    public Apple(GridMap gridmap){
        random = new Random();
        try {
            appleImg = ImageIO.read(new File("sprites/food/apple.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
        // addApple();
        
    }

    public void addApple(){
        newAppleX = random.nextInt(40);
        newAppleY = random.nextInt(40);
    }

    
    protected void draw(Graphics2D g2d) {
      g2d.drawImage(appleImg, appleX, appleY, null);

    }

    protected void tick(){
        addApple();
        appleX = GridMap.cellLayout[newAppleX][newAppleY][0];
        appleY = GridMap.cellLayout[newAppleX][newAppleY][1];
        
    }



}