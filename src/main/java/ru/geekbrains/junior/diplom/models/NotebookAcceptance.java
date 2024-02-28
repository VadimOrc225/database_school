package ru.geekbrains.junior.diplom.models;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.List;

public class NotebookAcceptance extends JFrame implements LoggerView {
    public static final int WIDTH = 480;
    public static final int HEIGHT = 300;
    JButton btnDeleteNotebook;
    JTextField notebookNumber;
    JPanel headerPanel;
    JTextArea log;

    public NotebookAcceptance() {
        // Создание фабрики сессий
        SessionFactory sessionFactory1 = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Notebook.class)
                .buildSessionFactory();
        setSize(WIDTH, HEIGHT);
        setLocation(890, 80);
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
                    int counter = 0;
                    try {
                        session1.beginTransaction();
                        String readDataSQL = "SELECT * FROM Notebooks;";
                        List<Notebook> notebooksOnHandsList = (List<Notebook>) session1.createSQLQuery(readDataSQL).addEntity(Notebook.class).list();
                        log.setText("");
                        log.append("СПИСОК ВЫДАННЫХ НОУТБУКОВ:");
                        for (Notebook notebookX : notebooksOnHandsList) {
                            log.append("\n" + notebookX);
                            if (notebookX.getNumber() == Integer.parseInt(notebookNumber.getText())) {
                                Notebook retrievedNotebook = notebookX;
                                log.append("\n" + "Удаляется из списка (принимается) ноутбук " + retrievedNotebook.getNumber());
                                counter++;
                            }

                        }
                        if(counter==0) { log.append("\n" + "Ноутбука " + notebookNumber.getText()+
                                " нет в списке выданных! Введите другой номер!");}

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

        add(btnDeleteNotebook = new JButton("Удалить ноутбук из списка выданных"), BorderLayout.SOUTH);

        btnDeleteNotebook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (true) {
                    LocalDateTime currentDateTime = LocalDateTime.now();

                    // Удаление данных
                    Session session2 = sessionFactory1.getCurrentSession();
                    try {
                        // Начало транзакции
                        session2.beginTransaction();
                        log.setText("");
                        String numberOfNotebook = notebookNumber.getText();
                        String readDataSQL1 = "DELETE FROM Notebooks WHERE number=" + numberOfNotebook;
                        session2.createSQLQuery(readDataSQL1).executeUpdate();
                        log.append("\n" + "Ноутбук " +  numberOfNotebook + " удален из списка выданных");
                        saveInLog("Ноутбук " +  numberOfNotebook + " удален из списка выданных, " + currentDateTime.toString());
                        // Коммит транзакции
                        session2.getTransaction().commit();


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
        String LOG_PATH = "./src/main/java/ru/geekbrains/junior/diplom/log.txt";
        try (FileWriter writer = new FileWriter(LOG_PATH, true)) {
            writer.write(text);
            writer.write("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}