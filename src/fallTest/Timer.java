/*Name:	Guitar Hero Project
 *Description: Contains the method used to determine how long the user has been playing, 
 *             used to determine when to send notes
 */
package fallTest;


public class Timer
{
    private long timeStart = System.currentTimeMillis();
    private int bpm;

    public Timer(int newBpm) {
        bpm = newBpm;
    }

    public double time() {
        return ((double)(System.currentTimeMillis()-timeStart))*(bpm/60000.0);
    }
}
