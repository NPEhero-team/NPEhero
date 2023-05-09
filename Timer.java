/*Name:	Guitar Hero Project
 *Description: Contains the method used to determine how long the user has been playing, 
 *             used to determine when to send notes
 */
package cs;


public class Timer
{
    private long timeStart = System.currentTimeMillis();
    
    public int time() {
        return (int)(System.currentTimeMillis()-timeStart);
    }
}
