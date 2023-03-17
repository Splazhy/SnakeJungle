import javax.sound.sampled.*;
import java.io.IOException;

import java.io.File;
public class SoundEffects{

    public static void playMusic() {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("sprites/SoundEffects/Ancient Jungle Ruins!-byHeatleyBros.wav"));
            AudioFormat format = inputStream.getFormat();
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            FloatControl volume = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-9.0f);
            line.start();
            while (bytesRead != -1 && GamePanel.state == State.MENU) {
                bytesRead = inputStream.read(buffer, 0, buffer.length);
                if (bytesRead >= 0) {
                    line.write(buffer, 0, bytesRead);
                }
                if(GamePanel.state == State.PLAYZONE){
                    line.close();
                }
            }
            
          } catch (Exception e) {
            System.err.println(e.getMessage());
          }

    }

    
    public static void playBitingSound() {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("sprites/SoundEffects/biting-an-apple.wav"));
            clip.open(inputStream);
            clip.start(); 
          } catch (Exception e) {
            System.err.println(e.getMessage());
          }
    }

    public static void playBotEatingSound(){
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("sprites/SoundEffects/BotsEating.wav"));
            clip.open(inputStream);
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(+6.0f);
            clip.start(); 
          } catch (Exception e) {
            System.err.println(e.getMessage());
          }
    }

    public static void playGameOverSound(){
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("sprites/SoundEffects/GAMEOVER-Sound.wav"));
            clip.open(inputStream);
            clip.start(); 
          } catch (Exception e) {
            System.err.println(e.getMessage());
          }
    }

    public static void playBotsDeath(){
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("sprites/SoundEffects/BotsDeath.wav"));
            clip.open(inputStream);
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(+6.0f);
            clip.start(); 
          } catch (Exception e) {
            System.err.println(e.getMessage());
          }
    }
    
}
