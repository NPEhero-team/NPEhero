package fallTest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JButton;

public class LaneInfo {
    Queue<NoteInfo> sends = new LinkedList<NoteInfo>();         //Queue that dictates when to send the notes
    ArrayList<Block> nodes = new ArrayList<Block>();       //Array list containing all the notes currently on the field
    public void addSends(int t) {
    	sends.add(new NoteInfo(t));
    }
    
    public void addNodes(int t) {
    	nodes.add(new Block());
    }
    
    public ArrayList<Block> getNodes() {
    	return nodes;
    }
    
    public Queue<NoteInfo> getSends() {
    	return sends;
    }
}
