package main;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LeaderboardEntry 
{
    private SimpleIntegerProperty score;
    private SimpleStringProperty name;
    private SimpleStringProperty date;

    //all below is required for table view
    public LeaderboardEntry(String name, int score, String date)
    {
        this.name = new SimpleStringProperty(name);
        this.score = new SimpleIntegerProperty(score);
        this.date = new SimpleStringProperty(date);
    }

    public int getScore() {
        return score.get();
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDate()
    {
        return date.get();
    }

    public void setDate(String date)
    {
        this.date = new SimpleStringProperty(date);
    }
}
