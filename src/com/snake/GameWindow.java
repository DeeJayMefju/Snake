package com.snake;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Główne okno gry
 */
public class GameWindow extends JFrame {
    private static Board board;
    private static Panel left;
    private static JPanel right;
    private static GameEngine engine;

    /**
     * W konstruktorze okna gry sa ustawiane parametry tego okna.
     * Obliczane sa proporcje ekranu a rozmiar okna gry jest zawsze w
     * proporcjach 16:9, ale zmaksymalizowany poziomo lub pionowo.
     * Tworzona jest takze plansza i dwa panele boczne wraz z ich
     * parametrami.
     *
     * @param parent silnik gry powolujacy okno
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public GameWindow(GameEngine parent) {
        engine = parent;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(false);
        setUndecorated(true);
        int xSize = ((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth());
        int ySize = ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        board = new Board(ySize, this);
        left = new Panel(board);
        right = new JPanel();

        setSize(xSize, ySize);
        setLocationRelativeTo(null);

        setResizable(false);
        getContentPane().setBackground(new Color(29, 24, 84));
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        board.setMinimumSize(new Dimension(ySize, ySize));
        board.setPreferredSize(new Dimension(ySize, ySize));
        board.setMaximumSize(new Dimension(ySize, ySize));
        board.setBorder(new LineBorder(Color.yellow, 3));
        board.requestFocusInWindow();
        left.setBorder(new LineBorder(Color.green, 3));
        right.setBorder(new LineBorder(Color.green, 3));

        left.setMaximumSize(new Dimension((getWidth() - ySize) / 2, getHeight()));
        left.setPreferredSize(new Dimension((getWidth() - ySize) / 2, getHeight()));

        right.setMaximumSize(new Dimension(((getWidth() - ySize) / 2), getHeight()));
        right.setPreferredSize(new Dimension(((getWidth() - ySize) / 2), getHeight()));

        board.setBackground(new Color(64, 52, 226, 222));
        left.setBackground(new Color(79, 0, 113, 222));
        right.setBackground(new Color(79, 0, 113, 222));

        getContentPane().add(left);
        getContentPane().add(board);
        getContentPane().add(right);
    }

    /**
     * zwraca prawy panel okna
     *
     * @return prawy panel
     */
    public JPanel getRight() {
        return right;
    }

    /**
     * Zwraca lewy panel okna
     *
     * @return lewy panel
     */
    public Panel getLeft() {
        return left;
    }

    /**
     * przekazuje polecenie rozpoczecia gry z silnika do planszy
     *
     * @param diff       poziom trudnosci
     * @param przeszkody czy maja byc generowane przeszkody
     * @param sciany     czy maja byc generowane sciany
     */
    public void startGame(int diff, boolean przeszkody, boolean sciany) {
        board.startGame(diff, przeszkody, sciany);
    }

    /**
     * przekazuje polecenie wstrzymania gry z silnika do planszy
     */
    public void pauseGame() {
        Board.pauseGame();
    }

    /**
     * przekazuje polecenie wznowienia gry z silnika do planszy
     */
    public void resumeGame() {
        board.resumeGame();
    }

    /**
     * przekazuje polecenie zakonczenia gry z silnika do planszy
     */
    public void stopGame() {
        Board.stopGame();
    }

    /**
     * Zwraca instancje silnika gry, ktory powolal okno
     *
     * @return silnik
     */
    public GameEngine getEngine() {
        return engine;
    }
}