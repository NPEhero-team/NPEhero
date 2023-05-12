/*Name:	Guitar Hero Project
 *Description: Contains the main game loop for gameplay
 */
package fallTest;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
//test
public class SongPlayer
{
    Timer time = new Timer();
    
    public static final int HEIGHT = 650;
    public static final int LENGTH = 400;
        
    private final int BLENGTH = LENGTH/7;
    private final int BHEIGHT = HEIGHT/20;
    
    JFrame frame = new JFrame("Guitar Hero");       //creates the frame
    
    JButton d = new JButton("D");                   //button declarations
    JButton f = new JButton("F");
    JButton space= new JButton("SPC");
    JButton j = new JButton("J");
    JButton k = new JButton("K");
    
    Queue<NoteInfo> dSends = new LinkedList<NoteInfo>();         //Queue that dictates when to send the notes
    ArrayList<NoteField> dLane = new ArrayList<NoteField>();     //Array list containing all the notes currently on the field
    ArrayList<JButton> dVis = new ArrayList<JButton>();          //Array list containing the visual representations of the notes in lanes

    Queue<NoteInfo> fSends = new LinkedList<NoteInfo>();
    ArrayList<NoteField> fLane = new ArrayList<NoteField>();
    ArrayList<JButton> fVis = new ArrayList<JButton>();
    
    Queue<NoteInfo> spaceSends = new LinkedList<NoteInfo>();
    ArrayList<NoteField> spaceLane = new ArrayList<NoteField>();
    ArrayList<JButton> spaceVis = new ArrayList<JButton>();
    
    Queue<NoteInfo> jSends = new LinkedList<NoteInfo>();
    ArrayList<NoteField> jLane = new ArrayList<NoteField>();
    ArrayList<JButton> jVis = new ArrayList<JButton>();
    
    Queue<NoteInfo> kSends = new LinkedList<NoteInfo>();
    ArrayList<NoteField> kLane = new ArrayList<NoteField>();
    ArrayList<JButton> kVis = new ArrayList<JButton>();
    
    Score score = new Score();
    
    /**
     * Establishes what the chart for the song is going to look like
     */
    public void loadSong() {
        dSends.add(new NoteInfo(4000));
        dSends.add(new NoteInfo(4333));
        dSends.add(new NoteInfo(4666));
        fSends.add(new NoteInfo(5000));
        kSends.add(new NoteInfo(5500)); 
        spaceSends.add(new NoteInfo(6000));
        jSends.add(new NoteInfo(6000));
        jSends.add(new NoteInfo(6250));
        dSends.add(new NoteInfo(6500));
        jSends.add(new NoteInfo(6750));
        spaceSends.add(new NoteInfo(7000));
        fSends.add(new NoteInfo(7500));
        jSends.add(new NoteInfo(7750));
        spaceSends.add(new NoteInfo(8000));
        fSends.add(new NoteInfo(8500));
        jSends.add(new NoteInfo(8500));
        dSends.add(new NoteInfo(9000));
        spaceSends.add(new NoteInfo(9000));
        kSends.add(new NoteInfo(9000));
        spaceSends.add(new NoteInfo(9500));
        
        kSends.add(new NoteInfo(10000));
        dSends.add(new NoteInfo(10000));
        kSends.add(new NoteInfo(10333));
        fSends.add(new NoteInfo(10333));
        kSends.add(new NoteInfo(10666));
        spaceSends.add(new NoteInfo(10666));
        dSends.add(new NoteInfo(11000));
        spaceSends.add(new NoteInfo(11000));
        dSends.add(new NoteInfo(11333));
        jSends.add(new NoteInfo(11333));
        dSends.add(new NoteInfo(11666));
        kSends.add(new NoteInfo(11666));
        spaceSends.add(new NoteInfo(12000));
    }
    
    
    /**
     * Creates the GUI used to play the game
     */
    public void createAndShowGui() {
        
        d.setBounds(1*BLENGTH, (5*HEIGHT)/6, BLENGTH, BHEIGHT);      //makes the button bounds for each button
        f.setBounds(2*BLENGTH, (5*HEIGHT)/6, BLENGTH, BHEIGHT);
        space.setBounds(3*BLENGTH, (5*HEIGHT)/6, BLENGTH, BHEIGHT);
        j.setBounds(4*BLENGTH, (5*HEIGHT)/6, BLENGTH, BHEIGHT);
        k.setBounds(5*BLENGTH, (5*HEIGHT)/6, BLENGTH, BHEIGHT);
        d.setFocusable(false);                                       //makes it so you can't focus on the button
        f.setFocusable(false);
        space.setFocusable(false);
        j.setFocusable(false);
        k.setFocusable(false);


        frame.add(d);                           //adds the buttons to the frame
        frame.add(f);
        frame.add(space);
        frame.add(j);
        frame.add(k);
        
        frame.setSize(LENGTH, HEIGHT);          //sets the size of the frame
        frame.setLayout(null);                    
        frame.setVisible(true);                 //makes the frame visible
        
        
        while (true) {                          //TRY TO FIND A BETTER SOLUTION FOR THIS?? maybe something like sends.size() > 0 || lanes.size() > 0
            
            update(d, dSends, dLane, dVis, 'd', "dPress", 1);               //updates the provided lane
            update(f, fSends, fLane, fVis, 'f', "fPress", 2);
            update(space, spaceSends, spaceLane, spaceVis, ' ', "spacePress", 3);
            update(j, jSends, jLane, jVis, 'j', "jPress", 4);
            update(k, kSends, kLane, kVis, 'k', "kPress", 5);
            
            frame.repaint();    //updates the visuals every frame
            
            try {
                Thread.sleep(10);               //THIS IS PROBABLY NOT THE BEST WAY TO DO THIS
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }        
    }
    
    /**
     * Updates a lane. An update involves:
     * Checking to see if a note needs to be sent down a lane
     * Checking to see if the user hit the button
     * Checking to see if any notes have moved past the lane
     * @param sends     The sending queue for the given lane
     * @param lane      The place where note information is stored for notes currently in that lane
     * @param vis       The place where the visual representation for a note is stored in that lane
     * @param key       The button on the keyboard corresponding to the button for the lane being updated
     * @param id        The id for the action map
     * @param k         The lane number
     */
    private void update(JButton button, Queue<NoteInfo> sends, ArrayList<NoteField> lane, ArrayList<JButton> vis, char key, String id, int k) {
        if (!sends.isEmpty() && sends.peek().getTime()-time.time()<3) {    //checks if any notes in the queue need to be sent at this time
            lane.add(new NoteField());                                    //adds that note's information to the lane list
            
            vis.add(new JButton());                                        //creates a visual representation of that note in the visualizer list
            frame.add(vis.get(vis.size()-1));

            sends.remove();                                                //removes the note just sent from the sending queue
        }   
        
        if (lane.size() > 0) {                                                                             //if there are any notes in the lanes, tests for a button press
            button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), id);    //Input map and Action map setting
            button.getActionMap().put(id, new AbstractAction() {                                           //Defines what happens when the proper button is pressed
                public void actionPerformed(ActionEvent e)
                {
                    if (lane.size() > 0) {
                        int i = getClosestNote(lane);
                        int dist = (int)Math.abs(lane.get(i).goalDistance());
                    
                        lane.remove(i);                        //removes the notes and visual representation from the playing field when the button is pressed
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
                    
                        System.out.println("Score: " + score.getScore() + " Combo: " + score.getCombo());
                    }
                }
            });    
        }
        
        for (int i=0; i<lane.size(); i++) {    //goes through every note on the field
            lane.get(i).gameTick();            //moves every note down
            vis.get(i).setBounds(k*BLENGTH, HEIGHT-lane.get(i).getY(), BLENGTH, BHEIGHT);
            
            if (lane.size() > 0 && lane.get(i).getFailed()) {     //if the note has passed into the fail boundary, removes the note from the field
                score.miss();
                System.out.println(score.getScore() + " Combo: " + score.getCombo());

                
                lane.remove(i);
                frame.remove(vis.get(i));
                vis.remove(i);
                
                i--;
            }
        }
    }
    
    /**
     * Finds the note closest to the goal
     * @return the location in the array list of the closest note
     */
    private int getClosestNote(ArrayList<NoteField> searchLane) {
        int pos = 0;
        
        for (int i=0; i<searchLane.size(); i++) {
            if (Math.abs(searchLane.get(i).goalDistance()) < Math.abs(searchLane.get(pos).goalDistance())) {
                pos = i;
            }
        }
        
        return pos;
    }
}
