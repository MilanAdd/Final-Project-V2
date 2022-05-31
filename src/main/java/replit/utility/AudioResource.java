package replit.utility;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioResource implements Runnable {
  private String fileName;
  public AudioResource(String fileName){
    this.fileName=fileName;
  }

  /** Sets name for audio as a set file name or path
   * If it's not found, then a error message pops up
   */
  public void run (){
    File audioFile = new File(fileName);
    if (!audioFile.exists()) {
      System.out.println("Audio file not found:" + fileName);
      return;
    }

    /** Detects if there is an unsupported audio file inputted or if there is an IOException with the file */
    AudioInputStream audioInputStream = null; 
    try {
      audioInputStream = AudioSystem.getAudioInputStream(audioFile);
    } catch (UnsupportedAudioFileException e) {
      e.printStackTrace();
      return;
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    AudioFormat format = audioInputStream.getFormat(); 
    SourceDataLine audioLine = null; 
    DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

    /** Detects if where data is written has a line that is unavailable or whether there is an exception */
    try {
      audioLine = (SourceDataLine) AudioSystem.getLine(info);
      audioLine.open(format);
    } catch (LineUnavailableException e) {
      e.printStackTrace();
      return;
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    audioLine.start();
    int nBytesRead = 0;
    byte[] abData = new byte[100000];

    /** If bytes file is not -1 (meaning file exists), it can be read
     * If bytes is greater than or equal to 0, it can also be written
     * Catches any IOException
     * Regardless of exceptions, audio line is drained and closed
     */
    try {
      while (nBytesRead != -1) {
        nBytesRead = audioInputStream.read(abData, 0, abData.length);
        if (nBytesRead >= 0)
          audioLine.write(abData, 0, nBytesRead);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return;
    } finally {
      audioLine.drain();
      audioLine.close();
    }
  }
}
