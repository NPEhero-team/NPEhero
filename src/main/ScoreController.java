package main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ScoreController{

    int score = 0;
    int combo = 0;
    public StringProperty scoreProperty = new SimpleStringProperty("0");
    public StringProperty comboProperty = new SimpleStringProperty("0");

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
     * @param newScore: the score to be set
     */
    public void setScore(int newScore)
    {
        score = newScore;
        scoreProperty.setValue(newScore+"");
    }

    /**
     * @param newCombo: the combo to be set
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
