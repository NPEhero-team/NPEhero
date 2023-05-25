/*Name:	Guitar Hero Project
 *Description: Handles all the scoring for playing songs
 */
package gameplay;


public class Score
{
    private int combo=0;
    private int comboMultiplier=1;
    private int score=0;
    
    public void perfect() {
        score += 300*comboMultiplier;
        System.out.println("Perfect!");
    }
    
    public void good() {
        score += 100*comboMultiplier;
        System.out.println("Good");
    }
    
    public void miss() {
        combo = 0;
        comboMultiplier = 1;
        System.out.println("Miss");
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
