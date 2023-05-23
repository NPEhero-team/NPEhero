/*Name:	Guitar Hero Project
 *Description: Contains the info for when to send a note
 */
package fallTest;

import javafx.scene.paint.Color;

public class NoteInfo
{
    private int sendTime;
    private Color col;

    public NoteInfo(int t) {
        sendTime = t;
    }
    
    public int getTime() {
        return sendTime;
    }
    
    public Color getColor() {
        return col;
    }

    public int compareTo(NoteInfo other) {
        return sendTime - other.sendTime;
    }
}
