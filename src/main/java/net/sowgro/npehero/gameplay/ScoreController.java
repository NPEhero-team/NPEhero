package net.sowgro.npehero.gameplay;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.sowgro.npehero.main.Sound;

public class ScoreController{

    private int score = 0;
    private int combo = 0;
    private int comboMultiplier=1;
    public StringProperty scoreProperty = new SimpleStringProperty("0");
    public StringProperty comboProperty = new SimpleStringProperty("0");

    /**
     * Called when the user performs a perfect hit
     */
    public void perfect() {
        combo();
        score += 300*comboMultiplier;
        scoreProperty.setValue(score+"");
        comboProperty.setValue(combo +"");
        // System.out.println("Perfect!");
    }

    /**
     * called when the user performs an okay hit
     */
    public void good() {
        combo();
        score += 100*comboMultiplier;
        scoreProperty.setValue(score+"");
        comboProperty.setValue(combo+"");
        // System.out.println("Good");
    }

    /**
     * Called when the user misses a note
     */
    public void miss(boolean muted) {
        if (!muted) {
            Sound.playSfx(Sound.MISS);
        }
        combo = 0;
        comboMultiplier = 1;
        scoreProperty.setValue(score+"");
        comboProperty.setValue(combo+"");
        // System.out.println("Miss");
    }

    /*
     * Increments the combo by one
     */
    private void combo() {
        Sound.playSfx(Sound.HIT);
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

    /**
     * @return current score
     */
    public int getScore()
    {
        return score;
    }

    /**
     * @return current combo
     */
    public int getCombo()
    {
        return combo;
    }

    /**
     * @param newScore: the score to be set, only used in debug
     */
    public void setScore(int newScore)
    {
        score = newScore;
        scoreProperty.setValue(newScore+"");
    }

    /**
     * @param newCombo: the combo to be set, only used in debug
     */
    public void setCombo(int newCombo)
    {
        combo = newCombo;
        comboProperty.setValue(newCombo+"");
    }

    /**
     * prints values into console
     */
    public void print()
    {
        System.out.println("--");
        System.out.println(combo);
        System.out.println(score);
        System.out.println("");
        System.out.println(scoreProperty);
        System.out.println(comboProperty);
    }
}
