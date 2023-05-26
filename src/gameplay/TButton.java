//glowing block of color c (jfx node)

package gameplay;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TButton extends Rectangle
{
    private Color col;
    private Color fill;
    public TButton(Color c, double a, double b, int r)
    {
        super();
        
        col = c;
        fill = new Color(c.darker().getRed(), c.darker().getGreen(), c.darker().getBlue(), 0.45);
        super.setFill(fill);
        super.setWidth(a);
        super.setHeight(b);
        super.setArcHeight(r);
        super.setArcWidth(r);
        super.setStroke(col);
        super.setStrokeWidth(5);
    }

    public void setColor(Color c) {
        col = c;
        fill = new Color(c.darker().getRed(), c.darker().getGreen(), c.darker().getBlue(), 0.45);
        super.setFill(fill);
        super.setStroke(c);
    }

    public Color getFillColor() {
        return fill;
    }

    public Color getColor() {
        return col;
    }
}