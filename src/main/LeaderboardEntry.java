package main;

public class LeaderboardEntry 
{
    private int score;
    private String name;
    private String date;

    public LeaderboardEntry(String name, int score, String date)
    {
        this.name = name;
        this.score = score;
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public String getDate()
    {
        return date;
    }
}
