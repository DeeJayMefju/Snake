package com.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


/**
 * Game state panel
 */
public class Panel extends JComponent implements ActionListener {
    private static int score, time = 0;
    private static Timer seconds;
    private static Board board;
    private static String diff;

    /**
     * Creates new instance of Panel, sets the board field to an existing board
     *
     * @param b board
     */
    public Panel(Board b) {
        board = b;
    }

    /**
     * Stops updating the game state and resets the timer
     */
    public void set() {
        if (seconds != null && seconds.isRunning()) seconds.stop();
        seconds = new Timer(1000, this);
        diff = board.getDiff();
    }

    /**
     * Resets state to initial values
     */
    public void reset() {
        time = 0;
        if (seconds != null && seconds.isRunning()) seconds.stop();
        seconds = new Timer(1000, this);
        diff = board.getDiff();
        score = board.getScore();
    }

    /**
     * Starts the state updating
     */
    public void start() {
        if (!seconds.isRunning()) seconds.start();
    }

    /**
     * Repaints the panel
     *
     * @param g2 graphical 2D environment
     */
    @Override
    protected void paintComponent(Graphics g2) {
        super.paintComponent(g2);
        Graphics2D g = (Graphics2D) g2;
        try {
            g.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/geometry.otf")).deriveFont(Font.PLAIN, 50));
        } catch (FontFormatException | IOException e) {
            System.out.println("Nie udało się wczytać czcionki!");
        }
        g.setColor(Color.yellow);
        g.drawString("Czas: " + time, 30, 160);
        g.drawString("Wynik: " + score, 30, 240);
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 35));
        g.drawString("Poziom: " + diff, 30, 80);
    }

    /**
     * Fired every second, gets game state from the board, adds one second to counter and
     * repaints the panel.
     *
     * @param actionEvent timer tick event
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        score = board.getScore();
        diff = board.getDiff();
        time++;
        repaint();
    }
}