package net.sowgro.npehero.gameplay;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ScoreController {

    public IntegerProperty combo = new SimpleIntegerProperty(0);
    public IntegerProperty comboMultiplier = new SimpleIntegerProperty(1);
    public IntegerProperty score = new SimpleIntegerProperty(0);

    public ScoreController() {
        combo.addListener((_, _, _) -> {
            if      (combo.get() >= 30) { comboMultiplier.set(4); }
            else if (combo.get() >= 20) { comboMultiplier.set(3); }
            else if (combo.get() >= 10) { comboMultiplier.set(2); }
            else                        { comboMultiplier.set(1); }
        });
    }

    /**
     * Called when the user performs a perfect hit
     */
    public void perfect() {
        combo.set(combo.get() + 1);
        score.set(score.get() + 300 * comboMultiplier.get());
    }

    /**
     * called when the user performs an okay hit
     */
    public void good() {
        combo.set(combo.get() + 1);
        score.set(score.get() + 100 * comboMultiplier.get());
    }

    /**
     * Called when the user misses a note
     */
    public void miss() {
        combo.set(0);
    }
}
