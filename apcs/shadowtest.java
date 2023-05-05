package apcs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.Shape;

import java.awt.geom.AffineTransform;

public class shadowtest {
    public static void drawShadowedShape(Shape shape, Graphics2D g2d) {
        Color holdColor = g2d.getColor();
        g2d.setColor(Color.black);
        AffineTransform holdTransform = g2d.getTransform();
        // want the shadow to be one line width pixel offset
        float lineWidth = g2d.getStroke() instanceof BasicStroke ? ((BasicStroke) (g2d.getStroke())).getLineWidth()
                : 1.0f;
        //System.err.println("DrawingUtilities.drawShadowedShape(): lineWidth = "+lineWidth);
        g2d.translate(lineWidth, lineWidth);
        g2d.draw(shape);
        g2d.setColor(holdColor);
        g2d.setTransform(holdTransform);
        g2d.draw(shape);
    }
}