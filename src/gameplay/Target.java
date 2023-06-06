//glowing block of color c (jfx node)

package gameplay;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Target extends StackPane
{
    private Color col;
    private Color fill;
    private Text label;
    public Rectangle rect = new Rectangle();
    public Target(Color c, double a, double b, int r, char key)
    {
        label = new Text(key+"");
        label.getStyleClass().add("t3");
        label.scaleXProperty().bind(super.widthProperty().divide(50));
        label.scaleYProperty().bind(label.scaleXProperty());
        super.getChildren().addAll(rect,label);


        col = c;
        fill = new Color(c.darker().getRed(), c.darker().getGreen(), c.darker().getBlue(), 0.45);
        rect.setFill(fill);
        rect.setWidth(a);
        rect.setHeight(b);
        rect.setArcHeight(r);
        rect.setArcWidth(r);
        rect.setStroke(col);
        rect.setStrokeWidth(5);
    }

    public void setColor(Color c) {
        col = c;
        fill = new Color(c.darker().getRed(), c.darker().getGreen(), c.darker().getBlue(), 0.45);
        rect.setFill(fill);
        rect.setStroke(c);
    }

    public Color getFillColor() {
        return fill;
    }

    public Color getColor() {
        return col;
    }
}