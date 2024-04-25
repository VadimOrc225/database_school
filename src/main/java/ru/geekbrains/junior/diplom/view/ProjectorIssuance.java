package ru.geekbrains.junior.diplom.view;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.geekbrains.junior.diplom.model.Projector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.sql.Timestamp;

public class ProjectorIssuance extends JFrame implements LoggerView {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 200;
    JButton btnAddProjector;
    JTextField  studentLastName, projectorNumber, groupNumber;
    JTextArea log;
    JPanel headerPanel;

    public ProjectorIssuance() {
        // Создание фабрики сессий
        SessionFactory sessionFactory2 = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Projector.class)
                .buildSessionFactory();
        setSize(WIDTH, HEIGHT);
        setLocation(3, 450);
        setResizable(false);
        setTitle("ВЫДАЧА ПРОЕКТОРА. Заполните поля!");

        createPanelProjector(sessionFactory2);
        setVisible(true);
    }
    private void createPanelProjector(SessionFactory sessionFactory2) {
        add(createHeaderPanel(), BorderLayout.NORTH);
        log = new JTextArea();
        log.setEditable(false);
        add(new JScrollPane(log));
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
                        log.setText("");
                        session.save(projector);
                        Projector retrievedProjector = session.get(Projector.class, projector.getId());
                        log.append("Записано: " + retrievedProjector);
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

                }
            }
        });

        studentLastName = new JTextField("ВВЕДИТЕ ФАМИЛИЮ КУРСАНТА");
        studentLastName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n'){
                    studentLastName.setForeground(Color.GREEN);

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
        String LOG_PATH = "./src/main/java/ru/geekbrains/junior/diplom/log.txt";
        try (FileWriter writer = new FileWriter(LOG_PATH, true)) {
            writer.write(text);
            writer.write("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
