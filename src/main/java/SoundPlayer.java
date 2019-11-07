import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {


    //instance methods
    public void playSound(String filepath) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();

    }

}
