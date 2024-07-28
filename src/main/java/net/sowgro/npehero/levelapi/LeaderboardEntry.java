package net.sowgro.npehero.levelapi;

/**
 * Represents one players score in the leaderboard
 */
public class LeaderboardEntry
{
    public final int score;
    public final String name;
    public final String date;

    /**
     * Create a new LeaderboardEntry
     * @param name The name the player input after completing the level
     * @param score The score the player earned
     * @param date The date the player earned this score
     */
    public LeaderboardEntry(String name, int score, String date)
    {
        this.name = name;
        this.score = score;
        this.date = date;
    }
}
