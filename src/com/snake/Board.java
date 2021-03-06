package com.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Board object is responsible for the board but also the game state, animations and some game mechanics
 */
public class Board extends JPanel implements ActionListener {


    private static final int maxLength = 100;
    private static final int initialLength = 3;
    //współrzędne
    private static final int[] x = new int[maxLength];
    private static final int[] y = new int[maxLength];
    //private static final int[] x2 = new int[maxLength];
    //private static final int[] y2 = new int[maxLength];
    private static final ArrayList<Integer> obstaclesX = new ArrayList<>();
    private static final ArrayList<Integer> obstaclesY = new ArrayList<>();
    //parametry mechanizmu gry
    private static final Random redAppleGenerator = new Random();
    //zmienne dotyczace wymiarow i czasu
    private static int boardSize;
    private static int dotSize;
    private static int randomRange;
    private static int length = initialLength;//, lenght2 = 0;
    private static int delay = 30;
    private static int initialDelay = 30;
    private static int foodX, foodY, goldenX, goldenY, greenX, greenY, helmetX, helmetY;
    //zmienne dotyczace grafik
    private static Image headUp, headRight, headDown, headLeft;
    private static Image helmetUp, helmetRight, helmetDown, helmetLeft, helmetOff;
    private static Image tail;
    private static Image obstacle;
    private static Image redApple, greenApple, goldenApple;
    private static EventThread eventThread;
    private static int difficulty;
    private static boolean left, right, up, down;
    private static boolean directionLock, pause, inGame;
    private static boolean obstacles, walls, helmet;
    private static Timer mainTimer;
    private static GameWindow gameWindow;


    /**
     * Sets attributes of the board, adds keyboard listener, declares main timer of the game.
     * Creates random events thread, initializes all graphic files etc.
     *
     * @param size   board size (w = h)
     * @param window window that the board is located in
     */
    public Board(int size, GameWindow window) {
        boardSize = size;
        dotSize = boardSize / 20;
        randomRange = (boardSize - dotSize) / dotSize;
        addKeyListener(new KeyboardListener());
        mainTimer = new Timer(delay, this);
        gameWindow = window;
        eventThread = new EventThread(this, boardSize, dotSize, 0, obstaclesX, obstaclesY, obstacles);

        initImages();
    }

    /**
     * Stops the game, sets the inGame flag to false, disables direction, stops the main timer and event thread.
     */
    public static void stopGame() {
        inGame = false;
        mainTimer.stop();
        eventThread.kill();
        gameWindow.getLeft().set();
        right = false;
        left = false;
        up = false;
        down = false;
    }

    /**
     * Pauses the game, sets pause flag, pauses the random event thread and game state counter
     */
    public static void pauseGame() {
        pause = true;
        eventThread.pause();
        gameWindow.getLeft().set();
    }

    private void initImages() {
        ImageIcon headUpIcon = new ImageIcon("src/img/head_up.png");
        headUp = headUpIcon.getImage().getScaledInstance(dotSize, dotSize + (dotSize / 5), Image.SCALE_FAST);
        ImageIcon headRightIcon = new ImageIcon("src/img/head_right.png");
        headRight = headRightIcon.getImage().getScaledInstance(dotSize + (dotSize / 5), dotSize, Image.SCALE_FAST);
        ImageIcon headDownIcon = new ImageIcon("src/img/head_down.png");
        headDown = headDownIcon.getImage().getScaledInstance(dotSize, dotSize + (dotSize / 5), Image.SCALE_FAST);
        ImageIcon headLeftIcon = new ImageIcon("src/img/head_left.png");
        headLeft = headLeftIcon.getImage().getScaledInstance(dotSize + (dotSize / 5), dotSize, Image.SCALE_FAST);
        ImageIcon tailUpIcon = new ImageIcon("src/img/tail_up.png");
        tail = tailUpIcon.getImage().getScaledInstance(dotSize, dotSize, Image.SCALE_FAST);
        ImageIcon obstacleIcon = new ImageIcon("src/img/przeszkoda.png");
        obstacle = obstacleIcon.getImage().getScaledInstance(dotSize, dotSize, Image.SCALE_FAST);
        ImageIcon redAppleIcon = new ImageIcon("src/img/apple.png");
        Board.redApple = redAppleIcon.getImage().getScaledInstance(dotSize, dotSize + (dotSize / 5), Image.SCALE_FAST);
        ImageIcon greenAppleIcon = new ImageIcon("src/img/green.png");
        greenApple = greenAppleIcon.getImage().getScaledInstance(dotSize, dotSize + (dotSize / 5), Image.SCALE_FAST);
        ImageIcon goldenAppleIcon = new ImageIcon("src/img/golden.png");
        goldenApple = goldenAppleIcon.getImage().getScaledInstance(dotSize, dotSize + (dotSize / 5), Image.SCALE_FAST);
        ImageIcon helmetUpIcon = new ImageIcon("src/img/kask_up.png");
        helmetUp = helmetUpIcon.getImage().getScaledInstance(dotSize + 2 * (dotSize / 5), dotSize, Image.SCALE_FAST);
        ImageIcon helmetRightIcon = new ImageIcon("src/img/kask_right.png");
        helmetRight = helmetRightIcon.getImage().getScaledInstance(dotSize, dotSize + 2 * (dotSize / 5), Image.SCALE_FAST);
        ImageIcon helmetDownIcon = new ImageIcon("src/img/kask_down.png");
        helmetDown = helmetDownIcon.getImage().getScaledInstance(dotSize + 2 * (dotSize / 5), dotSize, Image.SCALE_FAST);
        ImageIcon helmetLeftIcon = new ImageIcon("src/img/kask_left.png");
        helmetLeft = helmetLeftIcon.getImage().getScaledInstance(dotSize, dotSize + 2 * (dotSize / 5), Image.SCALE_FAST);
        helmetOff = helmetUpIcon.getImage().getScaledInstance(dotSize, dotSize, Image.SCALE_FAST);
    }

    /**
     * Resets the game state, timer delay, snake length. Places the snake in the middle of the board. Sets bonuses
     * to be away from visible board. Deletes obstacles, sets new green apple location.
     */
    public void resetGame() {
        delay = initialDelay;
        length = initialLength;
        //lenght2 = 0;
        for (int z = 0; z < length; z++) {
            x[z] = ((boardSize / dotSize) / 2) * dotSize;
            y[z] = ((boardSize / dotSize) / 2) * dotSize;
        }
        setBonus(-dotSize, -dotSize, 0);
        setBonus(-dotSize, -dotSize, 1);
        setBonus(-dotSize, -dotSize, 2);
        obstaclesX.clear();
        obstaclesY.clear();
        setFood();
        mainTimer.setDelay(delay);
        gameWindow.getLeft().reset();
    }

    /**
     * Starts the game
     *
     * @param diff   difficulty level
     * @param check  obstacles checkbox is checked
     * @param check2 walls checkbox is checked
     */
    public void startGame(int diff, boolean check, boolean check2) {
        obstacles = check;
        walls = check2;
        difficulty = diff;
        initialDelay = 500 / difficulty;
        inGame = true;
        eventThread = new EventThread(this, boardSize, dotSize, diff, obstaclesX, obstaclesY, obstacles);
        eventThread.start();
        mainTimer.restart();
        gameWindow.getLeft().set();
        resetGame();
        repaint();
        requestFocusInWindow();
    }

    /**
     * Resumes the game
     */
    public void resumeGame() {
        pause = false;
        eventThread.cont();
        gameWindow.getLeft().start();
        requestFocus();
    }

    /**
     * Main timer listener responsible for repainting, moving the snake, checking collisions and food.
     *
     * @param e timer tick event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!pause) {
            directionLock = false;
            move();
            checkFood();
            checkCollision();
            repaint();
        }
    }

    private void setFood() {

        int randomInt = redAppleGenerator.nextInt(randomRange);
        foodX = ((randomInt * dotSize));

        randomInt = redAppleGenerator.nextInt(randomRange);
        foodY = ((randomInt * dotSize));
    }

    /**
     * Sets location of new bonus
     *
     * @param x         x-coordinate
     * @param y         y-coordinate
     * @param bonusType bonus type (0 - golden apple, 1 - green apple, 2 - helmet)
     */
    public void setBonus(int x, int y, int bonusType) {
        if (bonusType == 0) {
            goldenX = x;
            goldenY = y;
        }
        if (bonusType == 1) {
            greenX = x;
            greenY = y;
        }
        if (bonusType == 2) {
            helmetX = x;
            helmetY = y;
        }
    }

    private void checkFood() {
        //czerwone jablko
        if ((x[0] == foodX) && (y[0] == foodY)) {
            length++;
            setFood();
            delay = (int) (0.95 * delay);
            mainTimer.setDelay(delay);
        }
        /*if ((x2[0] == foodX) && (y2[0] == foodY)) {
            lenght2++;
            setFood();
            delay = (int) (0.95 * delay);
            mainTimer.setDelay(delay);
        }*/

        //zlote jablko = 3 czerwone
        if ((x[0] == goldenX) && (y[0] == goldenY)) {
            length += 3;
            x[length - 2] = x[length - 3];
            y[length - 2] = y[length - 3];
            x[length - 1] = x[length - 2];
            y[length - 1] = y[length - 2];
            delay = (int) (0.9 * delay);
            mainTimer.setDelay(delay);
            setBonus(-dotSize, -dotSize, 0);
        }
        /*if ((x2[0] == goldenX) && (y2[0] == goldenY)) {
            lenght2 += 3;
            x2[lenght2 - 2] = x2[lenght2 - 3];
            y2[lenght2 - 2] = y2[lenght2 - 3];
            x2[lenght2 - 1] = x2[lenght2 - 2];
            y2[lenght2 - 1] = y2[lenght2 - 2];
            delay = (int) (0.9 * delay);
            mainTimer.setDelay(delay);
            setBonus(-dotSize, -dotSize, 0);
        }*/


        //zielone jablko przyspiesza 3-krotnie na 3 sekundy
        if ((x[0] == greenX) && (y[0] == greenY)) {
            int oldDelay = delay;
            delay /= 3;
            mainTimer.setDelay(delay);
            new Timer(3000, actionEvent -> {
                delay = oldDelay;
                mainTimer.setDelay(delay);
            }).start();
            setBonus(-dotSize, -dotSize, 1);
        }

/*
        //zielone jablko dzieli weza na dwa
        if ((x[0] == greenX) && (y[0] == greenY) && lenght2 == 0) {
            if (length % 2 != 0) length++;
            length = length / 2;
            for (int i = length; i < 2 * length; i++) {
                x2[i - length] = x[i];
                y2[i - length] = y[i];
                lenght2 = length;
            }
            setBonus(-dotSize, -dotSize, 1);

            new Timer(10000, ActionEvent -> {
                length += lenght2;
                lenght2 = 0;
            }).start();
        }
*/
        //kask
        if (((x[0] == helmetX) && (y[0] == helmetY))) {
            helmet = true;
            setBonus(-dotSize, -dotSize, 2);
        }
        /*if (((x2[0] == helmetX) && (y2[0] == helmetY))) {
            helmet = true;
            setBonus(-dotSize, -dotSize, 2);
        }*/
    }

    /**
     * Repaints board, snake, bonuses etc
     *
     * @param g2 graphical environment
     */
    @Override
    public void paintComponent(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        if (inGame) {
            Image img = tail;
            g.drawImage(redApple, foodX, foodY - (dotSize / 5), this);
            g.drawImage(goldenApple, goldenX, goldenY - (dotSize / 5), this);
            g.drawImage(greenApple, greenX, greenY - (dotSize / 5), this);
            g.drawImage(helmetOff, helmetX, helmetY, this);

            for (int z = 1; z < length; z++) {
                g.drawImage(img, x[z], y[z], this);
            }
            //for (int z = 1; z < lenght2; z++) {
            //    g.drawImage(img, x2[z], y2[z], this);
            //}

            if (up) {
                img = headUp;
                g.drawImage(img, x[0], y[0] - dotSize / 5, this);
                //if (lenght2 > 0) g.drawImage(img, x2[0], y2[0] - dotSize / 5, this);
                if (helmet) {
                    g.drawImage(helmetDown, x[0] - dotSize / 5, y[0], this);
                    //if (lenght2 > 0) g.drawImage(helmetDown, x2[0] - dotSize / 5, y2[0], this);
                }

            } else if (right) {
                img = headRight;
                g.drawImage(img, x[0], y[0], this);
                //if (lenght2 > 0) g.drawImage(img, x2[0], y2[0], this);
                if (helmet) {
                    g.drawImage(helmetLeft, x[0], y[0] - dotSize / 5, this);
                    //if (lenght2 > 0) g.drawImage(helmetLeft, x2[0], y2[0] - dotSize / 5, this);
                }
            } else if (down) {
                img = headDown;
                g.drawImage(img, x[0], y[0], this);
                //if (lenght2 > 0) g.drawImage(img, x2[0], y2[0], this);
                if (helmet) {
                    g.drawImage(helmetUp, x[0] - dotSize / 5, y[0], this);
                    //if (lenght2 > 0) g.drawImage(helmetUp, x2[0] - dotSize / 5, y2[0], this);
                }
            } else if (left) {
                img = headLeft;
                g.drawImage(img, x[0] - dotSize / 5, y[0], this);
                //if (lenght2 > 0) g.drawImage(img, x2[0] - dotSize / 5, y2[0], this);
                if (helmet) {
                    g.drawImage(helmetRight, x[0], y[0] - dotSize / 5, this);
                    //if (lenght2 > 0) g.drawImage(helmetRight, x2[0], y2[0] - dotSize / 5, this);
                }
            } else {
                img = headUp;
                g.drawImage(img, x[0], y[0] - dotSize / 5, this);
                //if (lenght2 > 0) g.drawImage(img, x2[0], y2[0] - dotSize / 5, this);
            }

            for (int i = 0; i < obstaclesX.size(); i++) {
                g.drawImage(obstacle, obstaclesX.get(i), obstaclesY.get(i), this);
            }

            gameWindow.repaint();
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void move() {

        for (int z = length; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
            //x2[z] = x2[(z - 1)];
            //y2[z] = y2[(z - 1)];
        }

        for (int i = 0; i < dotSize; i++) {
            if (left) {
                x[0] -= 1;
                //x2[0] -= 1;
            }
            if (right) {
                x[0] += 1;
                //x2[0] += 1;
            }
            if (up) {
                y[0] -= 1;
                //y2[0] -= 1;
            }
            if (down) {
                y[0] += 1;
                //y2[0] += 1;
            }
        }
    }

    @SuppressWarnings("SuspiciousListRemoveInLoop")
    private void checkCollision() {

        //if (lenght2 == 0) {
        for (int z = length; z > 0; z--) {
            if ((z > initialLength) && ((x[0] == x[z]) && (y[0] == y[z]))) {
                stopGame();
                gameWindow.getEngine().showGameOver(length - 3, 0);
                return;
            }
        }
        //}


        if (walls) {
            if (y[0] >= boardSize) {
                stopGame();
                gameWindow.getEngine().showGameOver(length - 3, 1);
                return;
            }
            if (y[0] < 0) {
                stopGame();
                gameWindow.getEngine().showGameOver(length - 3, 1);
                return;
            }
            if (x[0] >= boardSize) {
                stopGame();
                gameWindow.getEngine().showGameOver(length - 3, 1);
                return;
            }
            if (x[0] < 0) {
                stopGame();
                gameWindow.getEngine().showGameOver(length - 3, 1);
            }
        } else {
            for (int z = length; z >= 0; z--) {
                if (x[z] >= boardSize) x[z] %= boardSize;
                if (y[z] >= boardSize) y[z] %= boardSize;
                if (x[z] < 0) x[z] += boardSize;
                if (y[z] < 0) y[z] += boardSize;
            }
            /*for (int z = lenght2; z >= 0; z--) {
                if (x2[z] >= boardSize) x2[z] %= boardSize;
                if (y2[z] >= boardSize) y2[z] %= boardSize;
                if (x2[z] < 0) x2[z] += boardSize;
                if (y2[z] < 0) y2[z] += boardSize;
            }*/
        }

        for (int i = 0; i < obstaclesX.size(); i++) {
            if (((x[0] == obstaclesX.get(i)) && (y[0] == obstaclesY.get(i))) /*|| ((x2[0] == obstaclesX.get(i)) && (y2[0] == obstaclesY.get(i)))*/) {
                if (!helmet) {
                    stopGame();
                    gameWindow.getEngine().showGameOver(length - 3, 2);
                    return;
                } else {
                    obstaclesX.remove(i);
                    obstaclesY.remove(i);
                    helmet = false;
                }
            }
        }
    }

    /**
     * returns current score
     *
     * @return score
     */
    public int getScore() {
        return length - 3;
    }

    /**
     * returns current difficulty level
     *
     * @return difficulty level
     */
    public String getDiff() {
        switch (difficulty) {
            case 1:
                return "Łatwy";
            case 2:
                return "Średni";
            case 3:
                return "Trudny";
        }
        return "Brak danych";
    }

    private static class KeyboardListener extends KeyAdapter {

        long pressed = 0;

        /**
         * Sets direction depending on pressed key and speeds up if holding the key
         *
         * @param e key pressed event
         */
        @Override
        public void keyPressed(KeyEvent e) {
            pressed++;
            if (!directionLock) {
                int key = e.getKeyCode();

                if ((key == KeyEvent.VK_LEFT) && !right && !pause) {
                    left = true;
                    up = false;
                    down = false;
                    directionLock = true;
                }

                if ((key == KeyEvent.VK_RIGHT) && (!left) && !pause) {
                    right = true;
                    up = false;
                    down = false;
                    directionLock = true;
                }

                if ((key == KeyEvent.VK_UP) && (!down) && !pause) {
                    up = true;
                    right = false;
                    left = false;
                    directionLock = true;
                }

                if ((key == KeyEvent.VK_DOWN) && (!up) && !pause) {
                    down = true;
                    right = false;
                    left = false;
                    directionLock = true;
                }

                if (pressed > 7) {
                    mainTimer.setDelay(delay / 2);
                }

                if (!pause) gameWindow.getLeft().start();

            }
        }

        /**
         * resets the speed after releasing the key
         *
         * @param e key released event
         */
        public void keyReleased(KeyEvent e) {
            pressed = 0;
            mainTimer.setDelay(delay);
        }
    }


}
