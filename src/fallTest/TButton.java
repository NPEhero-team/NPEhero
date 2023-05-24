//glowing block of color c (jfx node)

package fallTest;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.paint.*;

public class TButton extends Rectangle
{
    public TButton(Color c, double a, double b, int r)
    {
        super();
        
        Color newCol = new Color(c.darker().getRed(), c.darker().getGreen(), c.darker().getBlue(), 0.45);
        super.setFill(newCol);
        super.setWidth(a);
        super.setHeight(b);
        super.setArcHeight(r);
        super.setArcWidth(r);
        super.setStroke(c);
        super.setStrokeWidth(5);
    }
}