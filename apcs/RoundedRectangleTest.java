package apcs;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;

public class RoundedRectangleTest extends JFrame {
   public RoundedRectangleTest() {
      setTitle("RoundedRectangle Test");
      setSize(350, 275);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLocationRelativeTo(null);
      setVisible(true);
   }
   public void paint(Graphics g) {
      
      Graphics2D g2d = (Graphics2D) g;
      g2d.setPaint(Color.DARK_GRAY);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      /*
      Color holdColor = g2d.getColor();
      g2d.setColor(Color.black);
      AffineTransform holdTransform = g2d.getTransform();
      // want the shadow to be one line width pixel offset
      float lineWidth = g2d.getStroke() instanceof BasicStroke ? ((BasicStroke) (g2d.getStroke())).getLineWidth()
            : 1.0f;
      //System.err.println("DrawingUtilities.drawShadowedShape(): lineWidth = "+lineWidth);
      g2d.translate(lineWidth, lineWidth);
      g2d.fillRoundRect(10, 50, 150, 150, 50, 50); // to draw a rounded rectangle.
      g2d.setColor(holdColor);
      g2d.setTransform(holdTransform);
      */
      g2d.fillRoundRect(10, 50, 150, 150, 50, 50); // to draw a rounded rectangle.
      
   }
   public static void main(String []args) {
      new RoundedRectangleTest();
   }

   /*/from www .  j av  a2 s .c  o m
   * @param   shape   the shape to be drawn
   * @param   g2d   the drawing context
   */
   public static void drawShadowedShape(Shape shape, Graphics2D g2d) 
   {
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