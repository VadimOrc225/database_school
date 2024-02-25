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

public class ProjectorIssuance extends JFrame implements LoggerView {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    JButton btnAddProjector;
    JTextField  studentLastName, projectorNumber, groupNumber;
    String textSum = "";

    JPanel headerPanel;

    public ProjectorIssuance() {
        // Создание фабрики сессий
        SessionFactory sessionFactory2 = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Projector.class)
                .buildSessionFactory();
        setSize(WIDTH, HEIGHT/2);
        setLocation(95, 450);
        setResizable(false);
        setTitle("ВЫДАЧА ПРОЕКТОРА. Заполните поля!");

        createPanelProjector(sessionFactory2);
        setVisible(true);
    }
    private void createPanelProjector(SessionFactory sessionFactory2) {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(btnAddProjector = new JButton("Запись информации"), BorderLayout.SOUTH);

        btnAddProjector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (true) {

                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Projector projector = new Projector(studentLastName.getText(),groupNumber.getText(),
                            projectorNumber.getText(), timestamp);
                    saveInLog(String.valueOf(projector));
                    System.out.println(projector);
// Создание сессии
                    Session session = sessionFactory2.getCurrentSession();

                    try {
                        // Начало транзакции
                        session.beginTransaction();
                        session.save(projector);
                        System.out.println("Object projector saved successfully");
                        Projector retrievedNotebook = session.get(Projector.class, projector.getId());
                        System.out.println("Object projector retrieved successfully");
                        System.out.println("Retrieved projector object: " + retrievedNotebook);
                        // Коммит транзакции
                        session.getTransaction().commit();
                    } finally {
                        session.close();
                    }
                    writing();

                } else {

                }
            }
        });
    }
    private Component createHeaderPanel(){
        headerPanel = new JPanel(new GridLayout(2, 2));
        projectorNumber = new JTextField("НОМЕР ПРОЕКТОРА");
        projectorNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n'){
//                    try {
                        projectorNumber.getText();
                        projectorNumber.setForeground(Color.GREEN);
//                    }catch (NumberFormatException ex) {
//                        System.out.println("Введите число");

//                    }
                    textSum= textSum + projectorNumber.getText();
                }
            }
        });

        studentLastName = new JTextField("ВВЕДИТЕ ФАМИЛИЮ КУРСАНТА");
        studentLastName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n'){
                    studentLastName.setForeground(Color.GREEN);
                    textSum= textSum + " " + studentLastName.getText();  //To do
                }
            }
        });

        groupNumber = new JTextField("НОМЕР УЧЕБНОЙ ГРУППЫ");
        groupNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n'){
                    groupNumber.getText();
                    groupNumber.setForeground(Color.GREEN);
                    textSum = textSum + " " + groupNumber.getText();
                }

            }

        });
        headerPanel.add(projectorNumber);
        headerPanel.add(new JPanel());
        headerPanel.add(studentLastName);
        headerPanel.add(groupNumber);

        return headerPanel;
    }
    public void writing () {
        System.out.println("Проектор добавлен к выданным!");
    }



    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING){

        }
    }
private void closing (SessionFactory sessionFactory2){
    sessionFactory2.close();
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
