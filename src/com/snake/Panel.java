package com.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


/**
 * Panel stanu gry
 */
public class Panel extends JComponent implements ActionListener {
    private static int score, time = 0;
    private static Timer seconds;
    private static Board board;
    private static String diff;

    /**
     * W konstruktorze panelu stanu gry ustawiany jest
     * atrybut przechowujacy plansze gry.
     *
     * @param b plansza
     */
    public Panel(Board b) {
        board = b;
    }

    /**
     * Zatrzymanie aktualizowania stanu i ustawienie timera od nowa
     */
    public void set() {
        if (seconds != null && seconds.isRunning()) seconds.stop();
        seconds = new Timer(1000, this);
        diff = board.getDiff();
    }

    /**
     * Reset do stanu poczatkowego
     */
    public void reset() {
        time = 0;
        if (seconds != null && seconds.isRunning()) seconds.stop();
        seconds = new Timer(1000, this);
        diff = board.getDiff();
        score = board.getScore();
    }

    /**
     * Uruchomienie aktualizacji stanu
     */
    public void start() {
        if (!seconds.isRunning()) seconds.start();
    }

    /**
     * metoda odmalowania panelu
     *
     * @param g2 srodowisko graficzne
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
     * Metoda wywolywana z kazdym tickiem timera czyli co sekunde.
     * Powoduje pobranie stanu gry z planszy, dodanie jednej sekundy
     * i odmalowanie panelu.
     *
     * @param actionEvent zdarzenie (tick timera)
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        score = board.getScore();
        diff = board.getDiff();
        time++;
        repaint();
    }
}