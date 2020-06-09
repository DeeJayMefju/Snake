package com.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * <h1>Silnik gry</h1>
 * <h2>Główna klasa zarządzająca interfejsem, oknami, akcjami przycisków.</h2>
 * Gra zrecznościowa polegajaca na sterowaniu wezem
 * tak, by nie uderzyc w sciane ani w przeszkoe
 * (jesli sa wlaczone) jednoczesnie zbierajac jak
 * najwiecej jablek (zlote = 3pkt, czerwone = 1pkt,
 * zielone przyspiesza, kask pozwala rozbic jedna przeszkode
 *
 * @author Mateusz Kędzierski
 * @version 1.1
 * @since 2020-06-08
 */
public class GameEngine implements ActionListener {

    //przyciski
    private static final UIButton start = new UIButton("GRAJ", 300, 100);
    private static final UIButton start2 = new UIButton("GRAJ", 300, 100);
    private static final UIButton high = new UIButton("WYNIKI", 300, 100);
    private static final UIButton help = new UIButton("POMOC", 300, 100);
    private static final UIButton exitMenu = new UIButton("WYJDŹ", 300, 100);
    private static final UIButton exitGame = new UIButton("MENU", 250, 100);
    private static final UIButton pause = new UIButton("PAUZA", 250, 100);
    private static final UIButton exitHigh = new UIButton("MENU", 300, 100);
    private static final UIButton exitInfo = new UIButton("WSTECZ", 300, 100);
    private static final UIButton exitOver = new UIButton("MENU", 300, 100);
    private static final UIButton exitDiff = new UIButton("WSTECZ", 300, 100);
    //inne elementy interfejsu
    private static final UIRadioButton easy = new UIRadioButton("Łatwy");
    private static final UIRadioButton med = new UIRadioButton("Średni");
    private static final UIRadioButton hard = new UIRadioButton("Trudny");
    private static final ButtonGroup radio = new ButtonGroup();
    private static final UICheckBox obstacles = new UICheckBox("Przeszkody");
    private static final UICheckBox walls = new UICheckBox("Ściany");
    //tablice przycisków
    private static final UIButton[] buttons = {start, start2, high, help, exitMenu, exitGame, pause, exitHigh, exitInfo, exitOver, exitDiff};
    private static final UIButton[] menuButtons = {start, high, help, exitMenu};
    //okna menu
    private static Window menu;
    private static Window difficulty;
    private static Window gameOver;
    private static Window info;
    private static Window highScore;
    //okno gry
    private static GameWindow game;
    private static UIText gameOverInfo;
    private static UIText gameOverScore;
    private static UIText gameovername;
    private static UITextField name;
    //zmienne mechanizmu gry
    private static int diff;
    private static int lastScore;
    private static Score score;


    /**
     * Konstruktor klasy GameEngine wywoluje funkcje inicjalizujace
     * wszystkie elementy interfejsu graficznego: menu, okno wyboru poziomu
     * trudnosci, okno pomocy, okno wynikow, glowne okno gry, przyciski itp
     */
    public GameEngine() {
        SwingUtilities.invokeLater(() -> {

            for (UIButton jb : buttons) jb.addActionListener(this);

            gameOverInfo = new UIText("", 40);
            gameOverScore = new UIText("", 40);
            gameovername = new UIText("Podaj swoje imię:", 40);
            name = new UITextField();


            radio.add(easy);
            radio.add(med);
            radio.add(hard);

            menu = initMenu();
            menu.setVisible(true);

            difficulty = initDifficulty();
            difficulty.setVisible(false);

            game = initGameWindow();
            game.setVisible(false);

            gameOver = initGameOver();
            gameOver.setVisible(false);

            highScore = initHighScore();
            highScore.setVisible(false);

            info = initInfo();
            info.setVisible(false);

        });
    }

    /**
     * Metoda main powoluje instancje silnika gry GameEngine
     *
     * @param args Nieuzywane.
     */
    public static void main(String[] args) {
        new GameEngine();
    }

    private static Window initMenu() {
        Window menu = new Window(450, 610);
        JPanel menuPanel = menu.getPane();
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(new UIHeader("Snake", 140));

        menuPanel.add(Box.createVerticalGlue());
        for (UIButton jb : menuButtons) {
            menuPanel.add(jb);
            menuPanel.add(Box.createVerticalGlue());
        }
        menu.add(menuPanel);

        menu.pack();

        return menu;
    }

    private static Window initDifficulty() {
        Window difficulty = new Window(450, 700);
        JPanel diffPanel = difficulty.getPane();
        diffPanel.setLayout(new BoxLayout(diffPanel, BoxLayout.PAGE_AXIS));

        diffPanel.add(Box.createVerticalGlue());
        diffPanel.add(new UIHeader("Poziom", 130));


        diffPanel.add(Box.createVerticalGlue());
        diffPanel.add(new UIText("Wybierz poziom trudności:", 35));

        easy.setSelected(true);
        diffPanel.add(easy);
        diffPanel.add(med);
        diffPanel.add(hard);

        diffPanel.add(Box.createVerticalGlue());

        UIText txt = new UIText("Wybierz opcje:", 35);
        txt.setAlignmentX(0.93F);
        diffPanel.add(txt);

        diffPanel.add(obstacles);

        walls.setSelected(true);
        diffPanel.add(walls);

        diffPanel.add(Box.createVerticalGlue());

        diffPanel.add(start2);
        diffPanel.add(Box.createVerticalGlue());
        diffPanel.add(exitDiff);
        diffPanel.add(Box.createVerticalGlue());

        difficulty.add(diffPanel);

        difficulty.pack();
        return difficulty;
    }

    private static Window initGameOver() {
        Window gameOver = new Window(600, 500);
        JPanel overPanel = gameOver.getPane();
        overPanel.setLayout(new BoxLayout(overPanel, BoxLayout.PAGE_AXIS));
        overPanel.add(Box.createVerticalGlue());
        overPanel.add(new UIHeader("Game Over", 110));

        overPanel.add(Box.createVerticalGlue());
        overPanel.add(gameOverInfo);

        overPanel.add(Box.createVerticalGlue());
        gameOverScore.setAlignmentX(0.5F);
        overPanel.add(gameOverScore);

        overPanel.add(Box.createVerticalGlue());
        gameovername.setAlignmentX(0.5F);
        try {
            gameovername.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/geometry.otf")).deriveFont(Font.PLAIN, 35));
        } catch (FontFormatException | IOException e) {
            System.out.println("Blad przy wczytywaniu czcionki!");
        }
        overPanel.add(gameovername);

        overPanel.add(Box.createVerticalGlue());
        name.setMaximumSize(new Dimension(200, 50));
        overPanel.add(name);

        overPanel.add(Box.createVerticalGlue());
        overPanel.add(exitOver);
        overPanel.add(Box.createVerticalGlue());

        gameOver.add(overPanel);

        gameOver.pack();
        return gameOver;
    }

    private static Window initHighScore() {
        Window highScore = new Window(600, 630);

        JPanel highPanel = highScore.getPane();
        highPanel.add(Box.createVerticalGlue());
        highPanel.add(new UIHeader("Wyniki", 140));
        highPanel.add(Box.createVerticalGlue());

        score = new Score();
        highPanel.add(score);

        highPanel.add(Box.createVerticalGlue());
        highPanel.add(Box.createRigidArea(new Dimension(400, 20)));
        highPanel.add(Box.createVerticalGlue());
        highPanel.add(exitHigh);
        highPanel.add(Box.createVerticalGlue());

        highScore.add(highPanel);
        highScore.pack();
        return highScore;
    }

    private static Window initInfo() {
        Window info = new Window(600, 700);
        JPanel infoPanel = info.getPane();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(new UIHeader("Pomoc", 140));

        UIText text = new UIText("Zasady gry:", 30);
        text.setForeground(Color.red);
        text.setMaximumSize(new Dimension((int) (0.9 * info.getWidth()), text.getHeight()));
        infoPanel.add(text);

        infoPanel.add(Box.createVerticalGlue());
        UIText zasady = new UIText("<html><p> Zbieraj jabłka, uważając na ściany i przeszkody! Pamiętaj aby nie zjeść siebie samego. Aby przyspieszyć, przytrzymaj klawisz kierunku. </p></html>", 25);
        zasady.setMaximumSize(new Dimension((int) (0.8 * info.getWidth()), zasady.getHeight()));
        infoPanel.add(zasady);
        infoPanel.add(Box.createVerticalGlue());

        UIText txt = new UIText("Bonusy:", 30);
        txt.setForeground(Color.red);
        txt.setMaximumSize(new Dimension((int) (0.9 * info.getWidth()), txt.getHeight()));
        infoPanel.add(txt);
        infoPanel.add(Box.createVerticalGlue());

        JPanel lista = new JPanel();

        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(new Color(100, 100, 255, 200));

        lista.add(Box.createVerticalGlue());
        UIText txt1 = new UIText(" - zwiększa długość o 3", 25);
        txt1.setIcon(new ImageIcon(new ImageIcon("src/golden.png").getImage().getScaledInstance(30, 36, 2)));
        txt1.setMaximumSize(new Dimension((int) (0.8 * info.getWidth()), txt.getHeight()));
        lista.add(txt1);
        lista.add(Box.createVerticalGlue());

        UIText txt2 = new UIText(" - zwiększa szybkość na 5 sekund", 25);
        txt2.setIcon(new ImageIcon(new ImageIcon("src/green.png").getImage().getScaledInstance(30, 36, 2)));
        txt2.setMaximumSize(new Dimension((int) (0.8 * info.getWidth()), txt.getHeight()));
        lista.add(txt2);
        lista.add(Box.createVerticalGlue());

        UIText txt3 = new UIText(" - pozwala zniszczyć jedną przeszkodę", 25);
        txt3.setIcon(new ImageIcon(new ImageIcon("src/kask_up.png").getImage().getScaledInstance(30, 36, 2)));
        txt3.setMaximumSize(new Dimension((int) (0.8 * info.getWidth()), txt.getHeight()));
        lista.add(txt3);

        lista.add(Box.createVerticalGlue());

        lista.setMaximumSize(new Dimension((int) (0.9 * info.getWidth()), 200));
        lista.setMinimumSize(new Dimension((int) (0.9 * info.getWidth()), 200));
        lista.setPreferredSize(new Dimension((int) (0.9 * info.getWidth()), 200));

        infoPanel.add(lista);

        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(exitInfo);
        infoPanel.add(Box.createVerticalGlue());

        info.add(infoPanel);

        info.pack();
        return info;
    }

    private GameWindow initGameWindow() {
        GameWindow game = new GameWindow(this);

        game.getRight().add(new UIHeader("Snake", 100));
        game.getRight().add(pause);
        game.getRight().add(exitGame);

        game.pack();
        return game;

    }

    /**
     * Metoda wywoływana w momencie wcisniecia ktoregos z przyciskow
     * interfejsu graficznego. Obsluguje kazdy przycisk z kazdego okna.
     * Rozpoczyna nowa gre w zaleznosci od wybranego poziomu trudnosci
     * i zaznaczonych opcji gry. Dodatkowo zmienia napis na przycisku
     * z PAUSE na RESUME i odwrotnie. Przycisk zamkniecia okna
     * Game Over powoduje dodanie wyniku do tabeli wynikow, jesli podane
     * przez gracza imie jest poprawnym ciagiem znakow
     *
     * @param e obslugiwane zdarzenie pochodzace od przycisku
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        //przyciski menu
        if (source == start) {
            difficulty.setVisible(true);
            menu.setVisible(false);
        } else if (source == high) {
            highScore.setVisible(true);
            menu.setVisible(false);
        } else if (source == help) {
            info.setVisible(true);
            menu.setVisible(false);
        } else if (source == exitMenu) {
            exitAll();

            //poziom trudnosci
        } else if (source == start2) {
            difficulty.setVisible(false);
            game.setVisible(true);
            if (easy.isSelected()) {
                game.startGame(1, obstacles.isSelected(), walls.isSelected());
                diff = 1;
            } else if (med.isSelected()) {
                game.startGame(2, obstacles.isSelected(), walls.isSelected());
                diff = 2;
            } else if (hard.isSelected()) {
                game.startGame(3, obstacles.isSelected(), walls.isSelected());
                diff = 3;
            }
        } else if (source == exitDiff) {
            difficulty.setVisible(false);
            menu.setVisible(true);


            //przyciski w grze
        } else if (source == pause) {
            if (pause.getText().equals("PAUZA")) {
                game.pauseGame();
                pause.setText("WZNÓW");
            } else {
                game.resumeGame();
                pause.setText("PAUZA");
            }
        } else if (source == exitGame) {
            game.stopGame();
            menu.setVisible(true);
            game.setVisible(false);
            gameOver.setVisible(false);
            highScore.setVisible(false);
            info.setVisible(false);

            //high score
        } else if (source == exitHigh) {
            menu.setVisible(true);
            game.setVisible(false);
            gameOver.setVisible(false);
            highScore.setVisible(false);
            info.setVisible(false);

            //help
        } else if (source == exitInfo) {
            menu.setVisible(true);
            info.setVisible(false);
            game.setVisible(false);
            gameOver.setVisible(false);
            highScore.setVisible(false);

            //game over
        } else if (source == exitOver) {
            info.setVisible(false);
            game.setVisible(false);
            gameOver.setVisible(false);
            highScore.setVisible(false);
            menu.setVisible(true);
            if (!name.getText().isEmpty()) score.addScore(name.getText(), lastScore, diff);
        }
    }


    private void exitAll() {
        menu.dispose();
        difficulty.dispose();
        highScore.dispose();
        gameOver.dispose();
        info.dispose();
        game.stopGame();
        game.dispose();
        gameOver.dispose();
    }

    /**
     * Metoda wyswietla okno Game Over. Jest wywolywana posrednio
     * przez obiekt planszy. Wypisuje odpowiedni komunikat odpowiadajacy
     * przyczynie smierci weza i wynik osiagniety przez gracza.
     *
     * @param score wynik
     * @param way   sposob smierci
     */
    public void showGameOver(int score, int way) {
        String[] over = {
                "Zjadłeś sam siebie!",
                "Uderzyłeś głową w mur!",
                "Zaliczyłeś przeszkodę!"};
        gameOverInfo.setText(over[way]);
        gameOverInfo.setForeground(Color.red);

        gameOverScore.setText("Twój wynik: " + score);
        gameOverScore.setForeground(Color.blue);

        lastScore = score;

        gameOver.setVisible(true);

    }

}
