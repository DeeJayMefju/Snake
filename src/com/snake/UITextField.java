package com.snake;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Pole tekstowe
 */
public class UITextField extends JTextField {
    /**
     * Ustawienie parametrow wygladu pola tekstowego
     */
    public UITextField() {
        setMaximumSize(new Dimension(250, 50));

        setBackground(new Color(110, 181, 255));
        setForeground(new Color(255, 0, 0));

        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/geometry.otf"));
            font = font.deriveFont(Font.PLAIN, 25);
        } catch (FontFormatException e) {
            System.out.println("Font format exception!");
        } catch (IOException e) {
            System.out.println("Font file error!");
        }
        setFont(font);


    }
}
