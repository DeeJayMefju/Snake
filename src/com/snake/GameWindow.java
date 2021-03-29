package com.snake;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Main game window
 */
public class GameWindow extends JFrame {
    private static Board board;
    private static Panel left;
    private static JPanel right;
    private static GameEngine engine;

    /**
     * Creates new instance of GameWindow, sets parameters of window.
     * Calculates screen proportions and window size is set to 16:9
     * maximized vertically or horizontally.
     * Creates the board and side panels with their properties.
     *
     * @param parent GameEngine that generates the window
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
     * returns right panel
     *
     * @return right panel
     */
    public JPanel getRight() {
        return right;
    }

    /**
     * Returns left panel
     *
     * @return left panel
     */
    public Panel getLeft() {
        return left;
    }

    /**
     * Passes the starting event from engine to the board.
     *
     * @param diff      difficulty level
     * @param obstacles generation of obstacles
     * @param walls     generation of walls
     */
    public void startGame(int diff, boolean obstacles, boolean walls) {
        board.startGame(diff, obstacles, walls);
    }

    /**
     * passes pause event to the board
     */
    public void pauseGame() {
        Board.pauseGame();
    }

    /**
     * passes resume event to the board
     */
    public void resumeGame() {
        board.resumeGame();
    }

    /**
     * passes stop event to the board
     */
    public void stopGame() {
        Board.stopGame();
    }

    /**
     * Returns the instance of GameEngine that created the window
     *
     * @return Game engine
     */
    public GameEngine getEngine() {
        return engine;
    }
}