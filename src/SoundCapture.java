import javax.sound.sampled.*;
import java.io.*;

/**
 * A sample program is to demonstrate how to record sound in Java
 * author: www.codejava.net
 * https://www.codejava.net/coding/capture-and-record-sound-into-wav-file-with-java-sound-api
 */
public class SoundCapture {
	
    // path of the wav file
    private File wavFile;
 
    // format of audio file
    private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
 
    // the line from which audio data is captured
    private TargetDataLine line;
    
    public SoundCapture(String filePath) {
    	setFilePath(filePath);
    }
    
    public void setFilePath(String filePath) {
    	wavFile = new File(filePath);
    }
    /**
     * Defines an audio format
     */
    private AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                                             channels, signed, bigEndian);
        return format;
    }
    
    /**
     * Captures the sound and record into a WAV file
     */
    private void start() throws LineUnavailableException, IOException{
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
 
            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
            	throw new LineUnavailableException();
                //System.out.println("Line not supported");
                //System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();   // start capturing
 
            //System.out.println("Start capturing...");
 
            AudioInputStream ais = new AudioInputStream(line);
 
            //System.out.println("Start recording...");
 
            // start recording
            AudioSystem.write(ais, fileType, wavFile);
 
        } catch (LineUnavailableException ex) {
        	throw ex;
            //ex.printStackTrace();
        } catch (IOException ioe) {
        	throw ioe;
            //ioe.printStackTrace();
        }
    }
 
    /**
     * Closes the target data line to finish capturing and recording
     */
    private void finish() {
        line.stop();
        line.close();
        //System.out.println("Finished");
    }
 
    /**
     * Entry to run the program
     */
    public void startCapture(final long recordTime) throws LineUnavailableException, IOException{
        // creates a new thread that waits for a specified
        // of time before stopping
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(recordTime);
                } catch (InterruptedException ex) {
                	
                }
                finish();
            }
        });
        stopper.start();
        // start recording
        try {
        	start();
        }
        catch (LineUnavailableException ex) {
        	throw ex;
        }
        catch (IOException ioe) {
        	throw ioe;
        }
    }

}