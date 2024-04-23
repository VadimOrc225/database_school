package ru.geekbrains.junior.diplom.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;

public class ServerWindow extends JFrame implements LoggerView {

    public static final int WIDTH = 475;
    public static final int HEIGHT = 500;


    JButton btnNotebookIssuance, btnNotebookAcceptance, btnProjectorIssuance, btnProjectorAcceptance,
            btnDopOperations;
    JTextArea log;


    private final String LOG_PATH = "./src/main/java/ru/geekbrains/junior/diplom/log.txt";

    //    public Logger logger;
    String textSum = "";

    public ServerWindow() {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(true);
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
        add(btnDopOperations = new JButton("Вывод списка произведенных операций"), BorderLayout.SOUTH);

        btnDopOperations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.setText("");
                log.setForeground(Color.BLACK);
                System.out.println("Логи выводятся!!!");
                log.append(getLog());

            }
        });
    }

    private Component createButtons() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        btnNotebookIssuance = new JButton("Выдача ноутбука");
        btnNotebookAcceptance = new JButton("Сдача ноутбука");
        btnProjectorIssuance = new JButton("Выдача проектора");
        btnProjectorAcceptance = new JButton("Сдача проектора");
        // кнопка выдачи ноутбука
        btnNotebookIssuance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.setForeground(Color.RED);
                log.setText("");
                log.append("\n" + "  Выдайте ноутбук! Посмотрите на номер ноутбука!");
                NotebookIssuance notebookWindow = new NotebookIssuance();

            }
        });
        // кнопка приема ноутбука
        btnNotebookAcceptance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.setForeground(Color.RED);
                log.setText("");
                log.append("\n" + "  Примите ноутбук! Проверьте комплектность");
                NotebookAcceptance notebookAcceptance = new NotebookAcceptance();
            }
        });
        // кнопка выдача проектора
        btnProjectorIssuance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.setForeground(Color.RED);
                log.setText("");
                log.append("\n" + "  Выдайте проектор! ");
                ProjectorIssuance projectorIssuance = new ProjectorIssuance();
            }
        });
        // кнопка приема проектора
        btnProjectorAcceptance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.setForeground(Color.RED);
                log.setText("");
                log.append("\n" + "  Примите проектор. Проверьте комплектность ");
                ProjectorAcceptance projectorAcceptance = new ProjectorAcceptance();
            }
        });

        //добавляем кнопки на панель
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
