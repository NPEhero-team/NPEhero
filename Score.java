/*Name:	Guitar Hero Project
 *Description: Handles all the scoring for playing songs
 */
package cs;


public class Score
{
    private int combo=0;
    private int comboMultiplier=1;
    private int score=0;
    
    public void perfect() {
        score += 5*comboMultiplier;
        System.out.println("perfect");
    }
    
    public void close() {
        score += comboMultiplier;
        System.out.println("close");
    }
    
    public void miss() {
        combo = 0;
        comboMultiplier = 1;
        System.out.println("miss");
    }
    public void combo() {
        combo++;
        
        if (combo == 2) {
            comboMultiplier = 2;
        }
        
        if (combo == 4) {
            comboMultiplier = 4;
        }
        
        if (combo == 8) {
            comboMultiplier = 8;
        }
    }
    
    public int getScore() {
        return score;
    }
    
    public int getCombo() {
        return combo;
    }
}
