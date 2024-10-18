//glowing block of color c (jfx node)

package net.sowgro.npehero.gameplay;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import net.sowgro.npehero.levelapi.Note;

/**
 * A block is a visual representation of a note on the screen. This is used both in the editor and in during the game play.
 */
public class Block extends Rectangle
{
    public Color color;
    public Note note;

    public Block(Color color, boolean useDropShadow, Note note)
    {
        this.note = note;
        this.color = color;
       
        super.setFill(color);

        if (useDropShadow) {
            enableDropShadow();
        }
    }

    public Block(Color color, Note note) {
        this(color, true, note);
    }

    public void enableDropShadow() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(100.0);
        dropShadow.setColor(color);
        dropShadow.setBlurType(BlurType.GAUSSIAN);
        super.setEffect(dropShadow);
    }
}