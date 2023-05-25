/*Name:	Guitar Hero Project
 *Description: Contains the info for when to send a note
 */
package gameplay;

import javafx.scene.paint.Color;

public class NoteInfo
{
    private double sendTime;
    private Color col;

    public NoteInfo(double t) {
        sendTime = t;
    }
    
    public double getTime() {
        return sendTime;
    }
    
    public Color getColor() {
        return col;
    }

    public double compareTo(NoteInfo other) {
        return sendTime - other.sendTime;
    }
}
