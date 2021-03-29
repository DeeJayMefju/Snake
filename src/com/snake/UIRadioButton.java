package com.snake;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Custom radio button
 */
public class UIRadioButton extends JRadioButton {
    /**
     * Ustawienie parametrow wygladu przycisku Radio
     * Creates new UIRadioButton and sets its visual params.
     *
     * @param text label to display next to the radio button
     */
    public UIRadioButton(String text) {

        setText(text);
        setAlignmentX(0.7F);
        setMaximumSize(new Dimension(250, 50));
        setFocusPainted(false);

        Image img = new ImageIcon("src/ui/radio.png").getImage().getScaledInstance(30, 30, Image.SCALE_FAST);
        Icon icon = new ImageIcon(img);
        setIcon(icon);

        img = new ImageIcon("src/ui/radio_sel.png").getImage().getScaledInstance(30, 30, Image.SCALE_FAST);
        icon = new ImageIcon(img);
        setSelectedIcon(icon);

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

        setBackground(new Color(46, 135, 255, 128));
        setForeground(new Color(255, 50, 50));
        setContentAreaFilled(false);
        setBorderPainted(false);
        //setOpaque(false);

        //setOpaque(true);
    }
}
