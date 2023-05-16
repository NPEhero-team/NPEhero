/*Name:	
 *Date:
 *Period:
 *Teacher:
 *Description:
 */
package test;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class KeyDetection extends AbstractAction
{
    long timeStart = System.currentTimeMillis();
    private char key;
    public KeyDetection(char ch){
        key = ch;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        // TODO Auto-generated method stub
        int time = (int)((System.currentTimeMillis()-timeStart));
        System.out.println(key + ": " + time);
    }
    
}
