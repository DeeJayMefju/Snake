package com.snake;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Custom checkbox
 */
public class UICheckBox extends JCheckBox {
    /**
     * Creates new instance of UICheckBox and sets its visual properties
     *
     * @param text text of the label displayed next to the checkbox
     */
    public UICheckBox(String text) {
        setText(text);
        setMaximumSize(new Dimension(250, 50));
        setAlignmentX(0.7F);

        Image img = new ImageIcon("src/ui/check.png").getImage().getScaledInstance(30, 30, Image.SCALE_FAST);
        Icon icon = new ImageIcon(img);
        setIcon(icon);

        img = new ImageIcon("src/ui/check_sel.png").getImage().getScaledInstance(30, 30, Image.SCALE_FAST);
        icon = new ImageIcon(img);
        setSelectedIcon(icon);

        setBackground(new Color(46, 135, 255));
        setForeground(new Color(255, 50, 50));
        setFocusable(false);
        setContentAreaFilled(false);
        setBorderPainted(false);

        Font ob = null;
        try {
            ob = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/geometry.otf"));
            ob = ob.deriveFont(Font.ITALIC, 35);
        } catch (FontFormatException e) {
            System.out.println("Font format exception!");
        } catch (IOException e) {
            System.out.println("Font file error!");
        }
        setFont(ob);


    }
}
