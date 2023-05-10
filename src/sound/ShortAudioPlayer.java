/*Name:	
 *Date:
 *Period:
 *Teacher:
 *Description:
 */
package sound;

  //Java program to play audio files. imports file scanning and various 
  //methods from the java audio class in order to do so.
  import java.io.File;
  import java.io.IOException;
   
  import javax.sound.sampled.AudioFormat;
  import javax.sound.sampled.AudioInputStream;
  import javax.sound.sampled.AudioSystem;
  import javax.sound.sampled.Clip;
  import javax.sound.sampled.DataLine;
  import javax.sound.sampled.LineEvent;
  import javax.sound.sampled.LineListener;
  import javax.sound.sampled.LineUnavailableException;
  import javax.sound.sampled.UnsupportedAudioFileException;

  public class ShortAudioPlayer implements LineListener
  {
      //indicates whether the playback completes or not
      boolean playCompleted;
      Clip audioClip;
      
      public void play(String audioFilePath)
      {
          File audioFile = new File(audioFilePath);
          
          try
          {
              //creates an audioInput object using the file we
              //declared earlier
              AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
              
              //gets the format of the audioStream object
              AudioFormat format = audioStream.getFormat();
              
              DataLine.Info info = new DataLine.Info(Clip.class, format);
              
              audioClip = (Clip) AudioSystem.getLine(info);
   
              audioClip.addLineListener(this);
   
              audioClip.open(audioStream);
               
              audioClip.start();
               
              while (!playCompleted) 
              {
                  // wait for the playback to complete
                  try 
                  {
                      Thread.sleep(1000);
                  } 
                  catch (InterruptedException ex) 
                  {
                      ex.printStackTrace();
                  }
              }    
              audioClip.close(); //stops the audio clip  
          }
          catch (UnsupportedAudioFileException ex) 
          {
              System.out.println("The specified audio file is not supported.");
              ex.printStackTrace();
          }
          catch (LineUnavailableException ex) 
          {
              System.out.println("Audio line for playing back is unavailable.");
              ex.printStackTrace();
          } 
          catch (IOException ex) 
          {
              System.out.println("Error playing the audio file.");
              ex.printStackTrace();
          }
      }
      
      
      /**
       * Listens to the START and STOP events of the audio line.
       */
      @Override
      public void update(LineEvent event) 
      {
          LineEvent.Type type = event.getType();
           
          if (type == LineEvent.Type.START) 
          {
              System.out.println("Playback started.");        
          } 
          else if (type == LineEvent.Type.STOP) 
          {
              playCompleted = true;
              System.out.println("Playback completed.");
          }
      }   
  }
