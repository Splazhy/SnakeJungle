import javax.sound.sampled.*;
import java.io.File;

// LOOKOUT: SoundPlayer stutters randomly
public class SoundPlayer {

  protected static float volumeVal = 0.0f;

  public static void playMusic() {
    try {
      AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("sound/Ancient Jungle Ruins!-byHeatleyBros.wav"));
      AudioFormat format = inputStream.getFormat();
      byte[] buffer = new byte[4096];
      int bytesRead = 0;
      DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
      SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
      line.open(format);
      FloatControl volume = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
      line.start();
      while (bytesRead != -1 && GamePanel.state.screen == 0) {
        volume.setValue(volumeVal);
        bytesRead = inputStream.read(buffer, 0, buffer.length);
        if (bytesRead >= 0) {
          line.write(buffer, 0, bytesRead);
        }
        if(GamePanel.state.screen != 0){
          line.close();
          break;
        }
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
  
  
  public static void playBitingSound() {
    try {
      Clip clip = AudioSystem.getClip();
      AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("sound/PlayerEating.wav"));
      clip.open(inputStream);
      // FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      // volume.setValue(volumeVal);
      long startTime = System.nanoTime();
      clip.start();
      System.out.println(System.nanoTime() - startTime);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
  
  public static void playBotEatingSound(){
    try {
      Clip clip = AudioSystem.getClip();
      AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("sound/BotsEating.wav"));
      clip.open(inputStream);
      FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      volume.setValue(volumeVal);
      clip.start(); 
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
  
  public static void playGameOverSound(){
    try {
      Clip clip = AudioSystem.getClip();
      AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("sound/GAMEOVER-Sound.wav"));
      clip.open(inputStream);
      FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      volume.setValue(volumeVal);
      clip.start(); 
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
  
  public static void playBotsDeath(){
    try {
      Clip clip = AudioSystem.getClip();
      AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("sound/BotsDeath.wav"));
      clip.open(inputStream);
      FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      volume.setValue(volumeVal);
      clip.start(); 
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
