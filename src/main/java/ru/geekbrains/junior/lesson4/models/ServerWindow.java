package ru.geekbrains.junior.lesson4.models;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;

public class ServerWindow extends JFrame implements LoggerView {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;



    JButton btnNotebookIssuance, btnNotebookAcceptance, btnProjectorIssuance, btnProjectorAcceptance,
            btnDopOperations;
    JTextArea log;


    private final String LOG_PATH = "./src/main/java/ru/geekbrains/junior/lesson4/log.txt";

//    public Logger logger;
    String textSum = "";

    public ServerWindow() {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("LABORATORY 951");
        setLocationRelativeTo(null);
        createPanel();
        setVisible(true);
    }

    private void createPanel() {

        add(createButtons(), BorderLayout.NORTH);
        log = new JTextArea();
        log.setEditable(false);
        add(new JScrollPane(log));
        add(btnDopOperations = new JButton("Другие операции"), BorderLayout.SOUTH);

        btnDopOperations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (true) {
                    System.out.println("РАБОТЫ НЕПОЧАТЫЙ КРАЙ!!!");
                    log.append("\n" + "Тут еще конь не валялся! ");
                } else {

                    saveInLog("Ммммм...");
                }
            }
        });
    }

    private Component createButtons() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        btnNotebookIssuance = new JButton("Выдача ноутбука");
        btnNotebookAcceptance = new JButton("Сдача ноутбука");
        btnProjectorIssuance = new JButton("Выдача проектора");
        btnProjectorAcceptance = new JButton("Сдача проектора");
        btnNotebookIssuance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (true) {
                    log.append("\n" + "Выдайте ноутбук! Посмотрите на номер ноутбука!");
                    NotebookIssuance notebookWindow = new NotebookIssuance();

                } else {

                    saveInLog("Что-то не так!");
                }
            }
        });

        btnNotebookAcceptance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (true) {

                    log.append("\n" + "Примите ноутбук! Проверьте комплектность");
                    NotebookAcceptance notebookAcceptance = new NotebookAcceptance();
                } else {

                    saveInLog("Что-то не так!");
                }
            }
        });
        btnProjectorIssuance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (true) {
                    log.append("\n" + "Выдайте проектор! ");
                } else {

                    saveInLog("Что-то не так!");
                }
            }
        });

        btnProjectorAcceptance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (true) {
                    System.out.println("ОТДАЙ проектор!");
                    log.append("\n" + "ОТДАЙ проектор ");
                } else {

                    saveInLog("Сервер запущен!");
                }
            }
        });


        panel.add(btnNotebookIssuance);
        panel.add(btnNotebookAcceptance);
        panel.add(btnProjectorIssuance);
        panel.add(btnProjectorAcceptance);

        return panel;
    }


    @Override
    public String getLog() {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(LOG_PATH);) {
            int c;
            while ((c = reader.read()) != -1) {
                stringBuilder.append((char) c);
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void saveInLog(String text) {
        try (FileWriter writer = new FileWriter(LOG_PATH, true)) {
            writer.write(text);
            writer.write("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
