/*Name:	Guitar Hero Project
 *Description: Contains the main game loop for gameplay
 */
package cs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class SongPlayer
{
    Timer time = new Timer();
    
    public static final int HEIGHT = 650;
    public static final int LENGTH = 400;
        
    private final int BLENGTH = LENGTH/6;
    private final int BHEIGHT = HEIGHT/20;
    
    JFrame frame = new JFrame("Guitar Hero");      //creates the frame
    JButton d = new JButton("D");       //creates the four button lanes
    
    Queue<NoteInfo> sends = new LinkedList<NoteInfo>();         //Queue that dictates when to send the notes
    ArrayList<NoteField> lanes = new ArrayList<NoteField>();    //Array list containing all the notes currently on the field
    ArrayList<JButton> vis = new ArrayList<JButton>();        //Array list containing the visual representations of the notes in lanes

    Score score = new Score();
    
    public void queueTest() {
        sends.add(new NoteInfo(1000));
        sends.add(new NoteInfo(2000));
        sends.add(new NoteInfo(3000));
        sends.add(new NoteInfo(4000));
        sends.add(new NoteInfo(5000));
        sends.add(new NoteInfo(6000));
        sends.add(new NoteInfo(7000));
        sends.add(new NoteInfo(8000));
        sends.add(new NoteInfo(9000));
    }
    
    
    /**
     * Creates the Gui used to play the game
     */
    public void createAndShowGui() {
        
        d.setBounds(1*BLENGTH, (5*HEIGHT)/6, BLENGTH, BHEIGHT);      //makes the button bounds for each button
        
        frame.add(d);                           //adds the buttons to the frame
        
        frame.setSize(LENGTH, HEIGHT);          //sets the size of the frame
        frame.setLayout(null);                    
        frame.setVisible(true);                 //makes the frame visible
        
        
        while (true) {                          //TRY TO FIND A BETTER SOLUTION FOR THIS?? maybe something like sends.size() > 0 || lanes.size() > 0
            
            if (!sends.isEmpty() && sends.peek().getTime()-time.time()<3) {    //checks if any notes in the queue need to be sent at this time
                lanes.add(new NoteField());                                    //adds that note's information to the lane list
                
                vis.add(new JButton());                                        //creates a visual representation of that note in the visualizer list
                frame.add(vis.get(vis.size()-1));

                sends.remove();                                                //removes the note just sent from the sending queue
            }   
            
            if (lanes.size() > 0) {                                                                             //if there are any notes in the lanes, tests for a button press
                d.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('d'), "dPress");    //Input map and Action map setting
                d.getActionMap().put("dPress", new AbstractAction() {                                           //Defines what happens when the proper button is pressed
                    public void actionPerformed(ActionEvent e)
                    {
                        int i = getClosestNote();
                        int dist = (int)Math.abs(lanes.get(i).goalDistance());
                        
                        lanes.remove(i);                        //removes the notes and visual representation from the playing field when the button is pressed
                        frame.remove(vis.get(i));
                        vis.remove(i);
                        
                        if (dist > 2*BHEIGHT) {                 //Determines what to add to the score depending on the proximity of the note
                            score.miss();
                        }
                        else if (dist > BHEIGHT) {
                            score.combo();
                            score.close();
                        }
                        else {
                            score.combo();
                            score.perfect();
                        }
                        
                        System.out.println(score.getScore() + " Combo: " + score.getCombo());
                    }
                });    
                d.setFocusable(false);                                                                          //makes it so you can't focus on the button
            }
            
            for (int i=0; i<lanes.size(); i++) {    //goes through every note on the field
                lanes.get(i).gameTick();            //moves every note down
                vis.get(i).setBounds(BLENGTH, HEIGHT-lanes.get(i).getY(), BLENGTH, BHEIGHT);
                
                if (lanes.size() > 0 && lanes.get(i).getFailed()) {     //if the note has passed into the fail boundary, removes the note from the field
                    score.miss();
                    System.out.println(score.getScore() + " Combo: " + score.getCombo());

                    
                    lanes.remove(i);
                    frame.remove(vis.get(i));
                    vis.remove(i);
                    
                    i--;
                }
            }
            
            frame.repaint();    //updates the visuals every frame
            
            try {
                Thread.sleep(10);
            } catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }        
    }
    
    /**
     * Finds the note closest to the goal
     * @return the location in the array list of the closest note
     */
    private int getClosestNote() {
        int pos = 0;
        
        for (int i=0; i<lanes.size(); i++) {
            if (Math.abs(lanes.get(i).goalDistance()) < Math.abs(lanes.get(pos).goalDistance())) {
                pos = i;
            }
        }
        
        return pos;
    }
}
