package com.snake;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Custom formatted text
 */
public class UIText extends JLabel {

    /**
     * Sets visual parameters of the text
     *
     * @param text text to display
     * @param size font size
     */
    public UIText(String text, int size) {
        setText(text);
        setAlignmentX(0.5F);
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/geometry.otf"));
            font = font.deriveFont(Font.PLAIN, size);
            setFont(font);
        } catch (FontFormatException e) {
            System.out.println("Font format exception!");
        } catch (IOException e) {
            System.out.println("Font file error!");
        }

        setForeground(new Color(8, 11, 93));

    }
}
