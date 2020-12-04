import java.io.*;
import javax.sound.sampled.*;

/**
 * This is an example program that demonstrates how to play back an audio file
 * using the Clip in Java Sound API.
 * @author www.codejava.net
 * https://www.codejava.net/coding/how-to-play-back-audio-in-java-with-examples
 */
public class SoundPlayback implements LineListener{
	
	/**
     * this flag indicates whether the playback completes or not.
     */
    private boolean playCompleted;
     
    /**
     * Play a given audio file.
     * @param audioFilePath Path of the audio file.
     */
    public void play(String audioFilePath) throws UnsupportedAudioFileException, LineUnavailableException, IOException{
        File audioFile = new File(audioFilePath);
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.addLineListener(this);
            audioClip.open(audioStream);
            audioClip.start();
            while (!playCompleted) {
                // wait for the playback completes
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    //ex.printStackTrace();
                }
            }
            audioClip.close();
        }
        catch (UnsupportedAudioFileException ex) {
        	throw ex;
            //System.out.println("The specified audio file is not supported.");
            //ex.printStackTrace();
        }
        catch (LineUnavailableException ex) {
        	throw ex;
            //System.out.println("Audio line for playing back is unavailable.");
            //ex.printStackTrace();
        }
        catch (IOException ex) {
        	throw ex;
            //System.out.println("Error playing the audio file.");
            //ex.printStackTrace();
        }
         
    }
     
    /**
     * Listens to the START and STOP events of the audio line.
     */
    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
        if (type == LineEvent.Type.START) {
            //System.out.println("Playback started.");
        }
        else if (type == LineEvent.Type.STOP) {
            playCompleted = true;
            //System.out.println("Playback completed.");
        }
    }

}