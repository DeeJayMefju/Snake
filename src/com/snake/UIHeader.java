package com.snake;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


/**
 * Nagłówek interfejsu
 */
public class UIHeader extends JLabel {

    /**
     * Ustawienie parametrow wygladu naglowkow
     *
     * @param text tekst naglowka
     * @param size rozmiar czcionki naglowka
     */
    public UIHeader(String text, int size) {

        setText(text);
        setAlignmentX(0.5F);
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/troika.otf"));
            font = font.deriveFont(Font.PLAIN, size);
            setFont(font);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        setForeground(new Color(0, 110, 0));
    }
}
