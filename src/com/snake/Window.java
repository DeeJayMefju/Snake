package com.snake;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Menu window
 */
public class Window extends JFrame {

    private final JPanel mainPanel = new JPanel();

    /**
     * Sets menu window size and style
     *
     * @param width  menu window width
     * @param height menu window height
     */
    public Window(int width, int height) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/geometry.otf"));
            font = font.deriveFont(Font.PLAIN, 30);
            UIManager.put("TextField.font", font);
        } catch (FontFormatException | IOException e) {
            System.out.println("Błąd przy wczytywaniu czcionki!");
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        mainPanel.setPreferredSize(getSize());
        setUndecorated(true);
        setFocusable(false);
        setLocationRelativeTo(null);
        mainPanel.setBackground(new Color(100, 255, 95));
    }

    /**
     * returns the main panel of menu window
     *
     * @return main panel
     */
    public JPanel getPane() {
        return mainPanel;
    }
}
