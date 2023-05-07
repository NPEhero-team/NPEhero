//glowing block of color c (jfx node)

package main;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block extends Rectangle
{
    public Block(Color c)
    {
        super();
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(200.0);
        dropShadow.setColor(c);
        dropShadow.setBlurType(BlurType.GAUSSIAN);
       
        super.setFill(c);
        super.setWidth(200);
        super.setHeight(100);
        super.setArcHeight(25);
        super.setArcWidth(25);
        super.setEffect(dropShadow);
    }
}