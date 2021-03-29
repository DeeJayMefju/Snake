package com.snake;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Custom button
 */
public class UIButton extends JButton {
    /**
     * Creates new UIButton and sets its visual properties
     *
     * @param text text to display on the button
     * @param w    width
     * @param h    height
     */
    public UIButton(String text, int w, int h) {
        setText(text);
        setAlignmentX(0.5F);
        setMaximumSize(new Dimension(w, h));
        setPreferredSize(new Dimension(w, h));
        setMinimumSize(new Dimension(w, h));
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/geometry.otf"));
            font = font.deriveFont(Font.BOLD, 60);
            setFont(font);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        setBackground(new Color(93, 151, 215, 255));
        setForeground(new Color(4, 0, 87));
        setBorder(BorderFactory.createLineBorder(Color.lightGray, 5, false));

        setFocusable(false);
    }

}
