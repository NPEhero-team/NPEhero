//glowing block of color c (jfx node)

package net.sowgro.npehero.gameplay;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Target extends StackPane
{
    private final Color col;
    private final Color fill;
    public Rectangle rect = new Rectangle();

    public Target(Color c, String key)
    {
        Text label = new Text(key);
        label.getStyleClass().add("t3");
        label.scaleXProperty().bind(super.widthProperty().divide(50));
        label.scaleYProperty().bind(label.scaleXProperty());
        super.getChildren().addAll(rect, label);


        col = c;
        fill = new Color(c.darker().getRed(), c.darker().getGreen(), c.darker().getBlue(), 0.45);
        rect.setFill(fill);
        rect.setStroke(col);
        rect.setStrokeWidth(4);
    }

    public Color getFillColor() {
        return fill;
    }

    public Color getColor() {
        return col;
    }
}