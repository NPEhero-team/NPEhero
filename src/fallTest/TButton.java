//glowing block of color c (jfx node)

package fallTest;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TButton extends Rectangle
{
    public TButton(Color c, double a, double b, int r)
    {
        super();
        
        super.setFill(c);
        super.setWidth(a);
        super.setHeight(b);
        super.setArcHeight(r);
        super.setArcWidth(r);
        super.setStroke(Color.BLACK);
    }
}