/*Name:	
 *Date:
 *Period:
 *Teacher:
 *Description:
 */
package apcs;
import java.awt.Graphics;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;  
import javax.swing.JFrame;  
public class Gui {
    public Gui()
    {  
        /* JFrame is a top level container (window)
         * where we would be adding our button
         */
        JFrame frame=new JFrame();  
                          
        // Creating Button          
        JButton b=new TButton("Click Me..");
        /* This method specifies the location and size
         * of button. In method setBounds(x, y, width, height)
         * x,y) are cordinates from the top left 
         * corner and remaining two arguments are the width
         * and height of the button.
         */
        b.setBounds(50,50,200, 50);  
             
        //Adding button onto the frame
        frame.add(b);  
        
        
                  
        // Setting Frame size. This is the window size
        frame.setSize(600,500);  
        
        frame.setLayout(null);  
        frame.setVisible(true);  
                  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        
    }  
}
