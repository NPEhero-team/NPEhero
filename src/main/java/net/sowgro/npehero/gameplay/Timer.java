/*Name:	Guitar Hero Project
 *Description: Contains the method used to determine how long the user has been playing, 
 *             used to determine when to send notes
 */
package net.sowgro.npehero.gameplay;


public class Timer
{
    private long timeStart = System.currentTimeMillis();
    private double bpm;

    public Timer(double newBpm) {
        bpm = newBpm;
    }

    public Timer() {
        bpm = 60000;
    }

    public double time() {
        return ((double)(System.currentTimeMillis()-timeStart)-2000)*(bpm/60000.0);
    }

    public String toString() {      
        return ""+((Math.round(10*(((double)(System.currentTimeMillis()-timeStart))*(bpm/60000.0))))/10.0);
    }
}
