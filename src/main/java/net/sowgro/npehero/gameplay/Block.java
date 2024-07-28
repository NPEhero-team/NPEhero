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

    public Block(Color color, double width, double height, int cornerRadius, boolean useDropShadow, Note note)
    {
        this.note = note;
        this.color = color;
       
        super.setFill(color);
        super.setWidth(width);
        super.setHeight(height);
        super.setArcHeight(cornerRadius);
        super.setArcWidth(cornerRadius);

        if (useDropShadow) {
            enableDropShadow();
        }
    }

    public Block(Color color, double width, double height, int cornerRadius) {
        this(color, width, height, cornerRadius, true, null);
    }

    public void enableDropShadow() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(200.0);
        dropShadow.setColor(color);
        dropShadow.setBlurType(BlurType.GAUSSIAN);
        super.setEffect(dropShadow);
    }
}