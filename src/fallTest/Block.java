//glowing block of color c (jfx node)

package fallTest;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block extends Rectangle
{
    public Block(Color c, double a, double b, int r)
    {
        super();
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(200.0);
        dropShadow.setColor(c);
        dropShadow.setBlurType(BlurType.GAUSSIAN);
       
        super.setFill(c);
        super.setWidth(a);
        super.setHeight(b);
        super.setArcHeight(r);
        super.setArcWidth(r);
        super.setEffect(dropShadow);
    }
}