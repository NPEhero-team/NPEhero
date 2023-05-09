/*Name:	Guitar Hero Project
 *Description: Contains the information for a single note on the field
 */
package cs;

public class NoteField
{
    private boolean failed = false;
    private final int NOTESPEED = 5;
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
    
    public int goalDistance() {
        return (yPos-((SongPlayer.HEIGHT)/6));
    }
    
    public boolean getFailed() {
        return failed;
    }
    
    public int getY() {
        return yPos;
    }
}
