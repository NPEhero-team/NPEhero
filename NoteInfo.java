/*Name:	Guitar Hero Project
 *Description: Contains the info for when to send a note
 */
package cs;


public class NoteInfo
{
    private int sendTime;
    
    public NoteInfo(int t) {
        sendTime = t;
    }
    
    public int getTime() {
        return sendTime;
    }
    
    public int compareTo(NoteInfo other) {
        return sendTime - other.sendTime;
    }
}
