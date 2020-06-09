package com.snake;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <h1>Wątek losowych zdarzeń</h1>
 * Wątek generujący losowe zdarzenia pojawiania sie bonusów
 * i ich znikania
 */
public class EventThread extends Thread {
    private static final Random randomEvent = new Random();
    private static Board board;
    private static int boardSize;
    private static int dotSize;
    private static int difficulty;
    private static ArrayList<Integer> obstaclesX;
    private static ArrayList<Integer> obstaclesY;
    private static boolean obstacles;
    private static boolean running;
    private static boolean lock = false;

    /**
     * W konstruktorze watku losowych zdarzen ustawiane sa parametry
     * potrzebne do generowania tych zdarzen
     *
     * @param board      obiekt planszy
     * @param boardSize  rozmiar planszy
     * @param dotSize    rozmiar punktu (segmentu weza)
     * @param difficulty poziom trudnosci
     * @param obstaclesX lista wspolrzednych x przszkod
     * @param obstaclesY lista wspolrzednych y przeszkod
     * @param obstacles  czy przeszkody maja byc generowane
     */
    public EventThread(Board board, int boardSize, int dotSize, int difficulty, ArrayList<Integer> obstaclesX, ArrayList<Integer> obstaclesY, boolean obstacles) {
        EventThread.board = board;
        EventThread.boardSize = boardSize;
        EventThread.dotSize = dotSize;
        EventThread.obstaclesX = obstaclesX;
        EventThread.obstaclesY = obstaclesY;
        EventThread.obstacles = obstacles;
        EventThread.difficulty = difficulty;
    }

    private static void updatePrzeszkody(int lvl) {
        obstaclesX.clear();
        obstaclesY.clear();
        for (int i = 0; i < lvl; i++) {
            int rx = randomEvent.nextInt(boardSize / dotSize) * dotSize;
            int ry = randomEvent.nextInt(boardSize / dotSize) * dotSize;
            obstaclesX.add(rx);
            obstaclesY.add(ry);
        }
    }

    /**
     * Glowna metoda watku dziala w petli i decyduje o tym, kiedy nalezy
     * wygenerowac zdarzenie, losuje to zdarzenie i wywoluje metody
     * odpowiedzialne za wykonanie go.
     */
    @Override
    public void run() {

        running = true;
        int lvl = 0;
        while (running) {

            while (lock) {
                yield();
            }
            try {
                TimeUnit.SECONDS.sleep(30 - 7 * difficulty);
            } catch (InterruptedException e) {
                System.out.println("Event thread interrupted!");
            }
            if (!running) break;
            int event;

            if (obstacles) event = randomEvent.nextInt(3);
            else event = randomEvent.nextInt(2);

            int x = randomEvent.nextInt(boardSize / dotSize) * dotSize;
            int y = randomEvent.nextInt(boardSize / dotSize) * dotSize;

            ActionListener actionListener = actionEvent -> board.setBonus(-dotSize, -dotSize, event);
            board.setBonus(x, y, 0);
            Timer timeout = new Timer(5000, actionListener);
            timeout.setRepeats(false);
            timeout.start();

            if (obstacles) updatePrzeszkody(lvl);
            lvl++;

        }
    }

    /**
     * Metoda odpowiedzialna za zakonczenie pracy watku
     */
    public void kill() {
        if (running) running = false;
        interrupt();
    }

    /**
     * Metoda odpowiedzialna za wstrzymanie pracy watku
     */
    public void pause() {
        lock = true;

    }

    /**
     * Metoda odpowiedzialna za wznowienie pracy watku
     */
    public void cont() {
        lock = false;
    }


}
