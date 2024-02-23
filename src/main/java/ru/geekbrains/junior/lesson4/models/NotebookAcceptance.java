package ru.geekbrains.junior.lesson4.models;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class NotebookAcceptance extends JFrame implements LoggerView {
    public static final int WIDTH = 450;
    public static final int HEIGHT = 300;
    JButton btnDeleteNotebook;
    JTextField notebookNumber;
    String textSum = "";

    JPanel headerPanel;
    JTextArea log;

    public NotebookAcceptance() {
        // Создание фабрики сессий
        SessionFactory sessionFactory1 = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Notebook.class)
                .buildSessionFactory();
        setSize(WIDTH, HEIGHT);
        setLocation(800, 280);
        setResizable(false);
        setTitle("ПРИЕМ НОУТБУКА. Заполните поле!");

        createPanelNotebook(sessionFactory1);
        setVisible(true);
    }

    private void createPanelNotebook(SessionFactory sessionFactory1) {
        headerPanel = new JPanel();
        notebookNumber = new JTextField("НОМЕР НОУТБУКА");
        notebookNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    Session session1 = sessionFactory1.getCurrentSession();
                    try {
                        session1.beginTransaction();
                        String readDataSQL = "SELECT * FROM Notebooks;";
                        List<Notebook> notebooksOnHandsList = (List<Notebook>) session1.createSQLQuery(readDataSQL).addEntity(Notebook.class).list();
                        // List<Course> coursesList =  query.list();
                        log.append("СПИСОК ВЫДАННЫХ НОУТБУКОВ:");
                        for (Notebook notebookX : notebooksOnHandsList) {
                            log.append("\n" + notebookX);
                            if (notebookX.getNumber() == Integer.parseInt(notebookNumber.getText())) {
                                Notebook retrievedNotebook = notebookX;
                                log.append("\n" + "Удаляется из списка (принимается) ноутбук " + retrievedNotebook);
                            }
                        }
                        Integer.parseInt(notebookNumber.getText());
                        notebookNumber.setForeground(Color.GREEN);
                    } catch (NumberFormatException ex) {
                        System.out.println("Введите число");

                    } finally {
                        session1.getTransaction().commit();

                        // Закрытие  сессии
                        session1.close();

                    }

                }
            }
        });
        headerPanel.add(notebookNumber);
        add(headerPanel, BorderLayout.NORTH);
        log = new JTextArea();
        log.setEditable(false);
        add(new JScrollPane(log));

        add(btnDeleteNotebook = new JButton("Убрать ноутбук из списка выданных"), BorderLayout.SOUTH);

        btnDeleteNotebook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (true) {
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    log.removeAll();
                    // Удаление данных
                    Session session2 = sessionFactory1.getCurrentSession();
                    try {
                        // Начало транзакции
                        session2.beginTransaction();

                        String numberOfNotebook = notebookNumber.getText();
                        String readDataSQL1 = "DELETE FROM Notebooks WHERE number=" + numberOfNotebook;

                        session2.createSQLQuery(readDataSQL1).executeUpdate();

                        log.append("\n" + "Ноутбук " +  numberOfNotebook + " удален из списка выданных");
                        saveInLog("Notebook " +  numberOfNotebook + " were deleted from Notebooks successfully" + currentDateTime);
                        // Коммит транзакции
                        session2.getTransaction().commit();
                        log.removeAll();
                        log.append(numberOfNotebook);
                    } finally {
                        // Закрытие  сессии

                        session2.close();

                    }

                } else {

                }

            }
        });
    }

    @Override
    public String getLog() {
        return null;
    }


    @Override
    public void saveInLog(String text) {
        String LOG_PATH = "./src/main/java/ru/geekbrains/junior/lesson4/log.txt";
        try (FileWriter writer = new FileWriter(LOG_PATH, true)) {
            writer.write(text);
            writer.write("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}