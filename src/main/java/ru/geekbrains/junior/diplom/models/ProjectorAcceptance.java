package ru.geekbrains.junior.diplom.models;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ProjectorAcceptance extends JFrame implements LoggerView {
    public static final int WIDTH = 475;
    public static final int HEIGHT = 300;
    JButton btnDeleteProjector;
    JTextField projectorNumber;
    JPanel headerPanel;
    JTextArea log;

    public ProjectorAcceptance() {
        // Создание фабрики сессий
        SessionFactory sessionFactory3 = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Projector.class)
                .buildSessionFactory();
        setSize(WIDTH, HEIGHT);
        setLocation(905, 380);
        setResizable(false);
        setTitle("ПРИЕМ ПРОЕКТОРА. Заполните поле!");

        createPanelProjector(sessionFactory3);
        setVisible(true);
    }

    private void createPanelProjector(SessionFactory sessionFactory3) {
        headerPanel = new JPanel();
        projectorNumber = new JTextField("НОМЕР ПРОЕКТОРА");
        projectorNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    Session session1 = sessionFactory3.getCurrentSession();
                    try {
                        session1.beginTransaction();
                        String readDataSQL = "SELECT * FROM Projectors;";
                        List<Projector> notebooksOnHandsList = (List<Projector>) session1.createSQLQuery(readDataSQL).addEntity(Projector.class).list();
                        log.setText("");
                        log.append("СПИСОК ВЫДАННЫХ ПРОЕКТОРОВ:");
                        for (Projector projectorX : notebooksOnHandsList) {
                            log.append("\n" + projectorX);
                            if (Objects.equals(projectorX.getNumber(), projectorNumber.getText())) {
                                Projector retrievedProjector = projectorX;
                                log.append("\n" + "Удаляется из списка (принимается) проектор " + retrievedProjector.getNumber());
                            }
                            else log.append("\n" + "Проектора " + projectorNumber.getText()+ " нет в списке выданных! Введите другой номер!");
                        }

                        projectorNumber.setForeground(Color.GREEN);
                    } catch (NumberFormatException ex) {
                        System.out.println("УПС");

                    } finally {
                        session1.getTransaction().commit();

                        // Закрытие  сессии
                        session1.close();

                    }

                }
            }
        });
        headerPanel.add(projectorNumber);
        add(headerPanel, BorderLayout.NORTH);
        log = new JTextArea();
        log.setEditable(false);
        add(new JScrollPane(log));

        add(btnDeleteProjector = new JButton("Удалить проектор из списка выданных"), BorderLayout.SOUTH);

        btnDeleteProjector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (true) {
                    LocalDateTime currentDateTime = LocalDateTime.now();

                    // Удаление данных
                    Session session2 = sessionFactory3.getCurrentSession();
                    try {
                        // Начало транзакции
                        session2.beginTransaction();
                        log.setText("");
                        String numberOfProjector = projectorNumber.getText();
                        String readDataSQL1 = "DELETE FROM Projectors WHERE number=" + numberOfProjector;

                        session2.createSQLQuery(readDataSQL1).executeUpdate();

                        log.append("\n" + "Проектор " +  numberOfProjector + " удален из списка выданных");
                        saveInLog("Проектор " +  numberOfProjector + " удален из списка выданных, " + currentDateTime.toString());
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