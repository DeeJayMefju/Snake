package com.snake;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Set of the high scores
 */
public class Score extends JScrollPane {
    private static final String[] naglowki = {"Imię", "Punkty", "Poziom"};
    private static Object[][] wyniki = new Object[0][3];
    private static JTable tabela = new JTable(wyniki, naglowki);

    /**
     * Creates instance of high score, updates the high score table, sets visual properties of table.
     */
    public Score() {
        updateArray();
        updateTable();
        setPreferredSize(new Dimension(530, 303));
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        setViewportView(tabela);
        tabela.setBackground(new Color(93, 151, 215, 255));
        tabela.setForeground(new Color(4, 0, 87));
        tabela.getTableHeader().setForeground(new Color(234, 0, 103));
        tabela.getTableHeader().setBackground(new Color(64, 75, 215));
    }

    private void updateArray() {
        java.util.List<Object[]> lines = new ArrayList<>();
        Scanner scan = null;
        try {
            scan = new Scanner(new File("highscore.csv"));
        } catch (FileNotFoundException e) {
            System.out.println("High Score file not found!");
        }

        int ile = 0;
        String thisLine;
        if (scan != null) {
            while (scan.hasNextLine()) {
                thisLine = scan.nextLine();
                ile++;
                if (thisLine != null) lines.add(thisLine.split(","));
            }
        }

        String blank = "Brak danych,0,b/d";
        while (ile < 5) {
            lines.add(blank.split(","));
            ile++;
        }

        Object[][] arr = new Object[ile][3];
        lines.toArray(arr);
        wyniki = arr;

        Arrays.sort(wyniki, (Object[] a, Object[] b) -> Integer.parseInt((String) b[1]) - Integer.parseInt((String) a[1]));

    }

    private void updateTable() {

        DefaultTableModel model = new DefaultTableModel(wyniki, naglowki) {

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) return Integer.class;
                else return getValueAt(0, columnIndex).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(model);
        tabela.setRowHeight(50);
        tabela.setFillsViewportHeight(true);
        tabela.setMaximumSize(new Dimension(530, 300));
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("src/fonts/geometry.otf")).deriveFont(Font.PLAIN, 25);
            tabela.setFont(font.deriveFont(Font.BOLD));
            tabela.getTableHeader().setFont(font);
            tabela.getTableHeader().setPreferredSize(new Dimension(tabela.getWidth(), tabela.getRowHeight()));
        } catch (FontFormatException e) {
            System.out.println("Font format exception!");
        } catch (IOException e) {
            System.out.println("Font file error!");
        }
        DefaultTableCellRenderer align = new DefaultTableCellRenderer();
        align.setHorizontalAlignment(JLabel.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(align);
        tabela.getColumnModel().getColumn(1).setCellRenderer(align);
        tabela.getColumnModel().getColumn(2).setCellRenderer(align);

        tabela.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(1).setMaxWidth(100);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(105);
        tabela.getColumnModel().getColumn(2).setMaxWidth(105);
        tabela.setAutoCreateRowSorter(false);
        tabela.setCellSelectionEnabled(false);

        setViewportView(tabela);
    }

    /**
     * Adds new score to the High Score table
     *
     * @param name  player's name
     * @param score player's score
     * @param diff  difficulty level
     */
    public void addScore(String name, int score, int diff) {
        FileWriter fw = null;
        try {
            fw = new FileWriter("highscore.csv", true);
        } catch (IOException e) {
            System.out.println("Błąd przy otwieraniu pliku wyników do zapisu!");
        }

        String difficulty;
        switch (diff) {
            case 1:
                difficulty = "łatwy";
                break;
            case 2:
                difficulty = "średni";
                break;
            case 3:
                difficulty = "trudny";
                break;
            default:
                difficulty = "b/d";
        }

        try {
            assert fw != null;
            fw.append(name).append(",").append(String.valueOf(score)).append(",").append(difficulty).append("\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("Błąd przy dodawaniu wyniku");
        }

        updateArray();
        updateTable();

    }
}
