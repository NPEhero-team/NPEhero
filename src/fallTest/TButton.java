//glowing block of color c (jfx node)

package fallTest;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TButton extends Rectangle
{
    private Color col;
    public TButton(Color c, double a, double b, int r)
    {
        super();
        
        col = new Color(c.darker().getRed(), c.darker().getGreen(), c.darker().getBlue(), 0.45);
        super.setFill(col);
        super.setWidth(a);
        super.setHeight(b);
        super.setArcHeight(r);
        super.setArcWidth(r);
        super.setStroke(c);
        super.setStrokeWidth(5);
    }

    public Color getColor() {
        return col;
    }
}