/*Name:	
 *Date:
 *Period:
 *Teacher:
 *Description:
 */
package cs;
import java.awt.*;

public class NoteTest
{
    private boolean failed = false;
    private int lane;
    private final int NOTESPEED = 1;
    private int yPos = SongPlayer.HEIGHT;
    
    public void gameTick() {
        if (!failed) {
            if (yPos > 0) {
                yPos -= NOTESPEED;
            }            
            else {
                failed = true;
            }
        }    
    }
    
    public boolean getFailed() {
        return failed;
    }
    
    public int getY() {
        return yPos;
    }
}
