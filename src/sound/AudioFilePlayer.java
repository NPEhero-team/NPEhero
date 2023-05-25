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
import java.util.Scanner;
  
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioFilePlayer 
{
  
    // to store current position
    Long currentFrame;
    Clip clip;
      
    // current status of clip
    String status;
      
    AudioInputStream audioInputStream;
    private String filePath;
  
    File audioFile;
    // constructor to initialize streams and clip
    public AudioFilePlayer(String newFilePath)
    {
        filePath = newFilePath;
        audioFile = new File(filePath);
        // create AudioInputStream object
        try {
            audioInputStream = 
                    AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        } catch (UnsupportedAudioFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
          
        // create clip reference
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
          
        // open audioInputStream to the clip
        try {
            clip.open(audioInputStream);
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
  
    // public static void main(String[] args) 
    // {
    //     try 
    //     {
    //         filePath = "src/assets/BookBetrayal.wav3";
    //         AudioFilePlayer audioPlayer = new AudioFilePlayer();
              
    //         audioPlayer.play();
    //         Scanner sc = new Scanner(System.in);
              
    //         while (true) //until the thread closes, ask the user what they want to do with the audio file
    //         {
    //             System.out.println("1. pause");
    //             System.out.println("2. resume");
    //             System.out.println("3. restart");
    //             System.out.println("4. stop");
    //             System.out.println("5. Jump to specific time");
    //             int c = sc.nextInt();
    //             audioPlayer.gotoChoice(c);
    //             if (c == 4)
    //             break;
    //         }
    //         sc.close();
    //     } 
          
    //     catch (Exception ex) 
    //     {
    //         System.out.println("Error with playing sound.");
    //         ex.printStackTrace();
          
    //       }
    // }
      
    // Work as the user enters his choice

    public void gotoChoice(int c)throws IOException, LineUnavailableException, UnsupportedAudioFileException 
    {
        //reads the users input and chooses what to do based on said input
        switch (c) 
        {
            case 1:
                pause();
                break;
            case 2:
                resumeAudio();
                break;
            case 3:
                restart();
                break;
            case 4:
                stop();
                break;
            case 5:
                System.out.println("Enter time (" + 0 + 
                ", " + clip.getMicrosecondLength() + ")");
                Scanner sc = new Scanner(System.in);
                long c1 = sc.nextLong();
                jump(c1);
                break;
      
        }
      
    }
      
    // Method to play the audio
    public void play() 
    {
        //start the clip
        clip.start();
          
        status = "play";
    }
      
    // Method to pause the audio
    public void pause() 
    {
        if (status.equals("paused")) 
        {
            System.out.println("audio is already paused");
            return;
        }
        this.currentFrame = this.clip.getMicrosecondPosition();
        clip.stop();
        status = "paused";
    }
      
    // Method to resume the audio
    public void resumeAudio() throws UnsupportedAudioFileException,
                                IOException, LineUnavailableException 
    {
        if (status.equals("play")) 
        {
            System.out.println("Audio is already "+
            "being played");
            return;
        }
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(currentFrame);
        this.play();
    }
      
    // Method to restart the audio
    public void restart() throws IOException, LineUnavailableException,
                                            UnsupportedAudioFileException 
    {
        clip.stop();
        clip.close();
        resetAudioStream();
        currentFrame = 0L;
        clip.setMicrosecondPosition(0);
        this.play();
    }
      
    // Method to stop the audio
    public void stop() throws UnsupportedAudioFileException,
    IOException, LineUnavailableException 
    {
        currentFrame = 0L;
        clip.stop();
        clip.close();
    }
      
    // Method to jump over a specific part
    public void jump(long c) throws UnsupportedAudioFileException, IOException,
                                                        LineUnavailableException 
    {
        if (c > 0 && c < clip.getMicrosecondLength()) 
        {
            clip.stop();
            clip.close();
            resetAudioStream();
            currentFrame = c;
            clip.setMicrosecondPosition(c);
            this.play();
        }
    }
      
    // Method to reset audio stream
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException, LineUnavailableException 
    {
        audioInputStream = AudioSystem.getAudioInputStream(
        new File(filePath).getAbsoluteFile());
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
  
}