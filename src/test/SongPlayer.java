/*Name:	
 *Date:
 *Period:
 *Teacher:
 *Description:
 */
package test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class SongPlayer
{
    public static final int HEIGHT = 650;
    public static final int LENGTH = 400;
    
    
    private final int BLENGTH = LENGTH/6;
    private final int BHEIGHT = HEIGHT/20;
    JFrame frame = new JFrame("Guitar Hero");      //creates the frame
    
    
    JButton d = new JButton("D");       //creates the four button lanes
    JButton f = new JButton("F");
    JButton h = new JButton("H");
    JButton j = new JButton("J");

    
    public void createAndShowGui() {
        
        d.setBounds(1*BLENGTH, (5*HEIGHT)/6, BLENGTH, BHEIGHT);      //makes the button bounds for each button
        f.setBounds(2*BLENGTH, (5*HEIGHT)/6, BLENGTH, BHEIGHT);     
        h.setBounds(3*BLENGTH, (5*HEIGHT)/6, BLENGTH, BHEIGHT);      
        j.setBounds(4*BLENGTH, (5*HEIGHT)/6, BLENGTH, BHEIGHT);     

        
        frame.add(d);                           //adds the buttons to the frame
        frame.add(f);
        frame.add(h);
        frame.add(j);
        
        frame.setSize(LENGTH, HEIGHT);          //sets the size of the frame
        frame.setLayout(null);                  //???    
        frame.setVisible(true);                 //makes the frame visible
        
        KeyDetection dAction = new KeyDetection('d');                                                   //creates an action for each char
        d.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('d'), "dPress");    //Input map and Action map setting
        d.getActionMap().put("dPress", dAction);
        d.setFocusable(false);                                                                          //makes it so you can't highlight the button
        
        KeyDetection fAction = new KeyDetection('f');
        f.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('f'), "fPress");
        f.getActionMap().put("fPress", fAction);
        f.setFocusable(false);
        
        KeyDetection hAction = new KeyDetection('h');
        h.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('h'), "hPress");
        h.getActionMap().put("hPress", hAction);
        h.setFocusable(false);

        KeyDetection jAction = new KeyDetection('j');
        j.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('j'), "jPress");
        j.getActionMap().put("jPress", jAction);
        j.setFocusable(false);
    }
    
    public void loop() {
        JButton note = new JButton();
        JButton test = new JButton();
        test.setBounds(200, 200, 100, 100);
        note.setBounds(BLENGTH, 0, BLENGTH, BHEIGHT);
        frame.add(note);
        frame.add(test);
        
        NoteTest a = new NoteTest();
        while (!a.getFailed()) {
            if (!a.getFailed()) {
                a.gameTick();
                note.setBounds(BLENGTH, HEIGHT-a.getY(), BLENGTH, BHEIGHT); //moves the note down every frame
                System.out.println(a.getFailed());
                //the computer runs too fast normally, force it to run at a certain fps
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (a.getFailed()) {
                frame.remove(note); //removes the note once its off the screen
            }
        }
    }
}
