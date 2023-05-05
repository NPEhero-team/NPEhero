package apcs;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;

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
      //DropShadowBorder shadow = new DropShadowBorder();
      
      Graphics2D g2d = (Graphics2D) g;
      g2d.setPaint(Color.DARK_GRAY);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.fillRoundRect(10, 50, 150, 150, 30, 30); // to draw a rounded rectangle.
      
      /*Graphics2D g2d = (Graphics2D) g;
      Color holdColor = g2d.getColor();
      g2d.setColor(Color.black);
      AffineTransform holdTransform = g2d.getTransform();
      // want the shadow to be one line width pixel offset
      float lineWidth;
      if (g2d.getStroke() instanceof BasicStroke)
      {
         lineWidth = ((BasicStroke) (g2d.getStroke())).getLineWidth();
      } 
      else 
      {
         lineWidth = 1.0f;
      }
      //System.err.println("DrawingUtilities.drawShadowedShape(): lineWidth = "+lineWidth);
      //g2d.translate(lineWidth, lineWidth);
      g2d.fillRoundRect(10, 50, 150, 150, 30, 30);
      //g2d.setColor(holdColor);
      //g2d.setTransform(holdTransform);
      //g2d.drawRoundRect(10, 50, 150, 150, 30, 30);
      */


   }
   public static void main(String []args) {
      new RoundedRectangleTest();
   }
   
   public static void drawShadowedShape(Shape shape, Graphics2D g2d) 
   {
      Color holdColor = g2d.getColor();
      g2d.setColor(Color.black);
      AffineTransform holdTransform = g2d.getTransform();
      // want the shadow to be one line width pixel offset
      float lineWidth;
      if (g2d.getStroke() instanceof BasicStroke)
      {
         lineWidth = ((BasicStroke) (g2d.getStroke())).getLineWidth();
      } 
      else 
      {
         lineWidth = 1.0f;
      }
      //System.err.println("DrawingUtilities.drawShadowedShape(): lineWidth = "+lineWidth);
      g2d.translate(lineWidth, lineWidth);
      g2d.draw(shape);
      g2d.setColor(holdColor);
      g2d.setTransform(holdTransform);
      g2d.draw(shape);
   }

   public static void drawShadowedShape2(Shape shape, Graphics2D g2d) {
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