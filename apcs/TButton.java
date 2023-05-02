package apcs;

import javax.swing.JButton;  
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Graphics;

public class TButton extends JButton{
    public TButton(String text) {
        super(text);
        super.setContentAreaFilled(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Color temp = super.getBackground();
        if (getModel().isPressed()) {
            g.setColor(temp.darker().darker());
        } else if (getModel().isRollover()) {
            g.setColor(temp.darker());
        } else {
            g.setColor(temp);
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
        super.setBorderPainted(false);
    }
}
