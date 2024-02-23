package ru.geekbrains.junior.lesson4.models;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Timestamp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.time.LocalDateTime;

public class NotebookIssuance extends JFrame implements LoggerView {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    JButton btnAddNotebook;
    JTextField  studentLastName, notebookNumber, groupNumber;
    String textSum = "";

    JPanel headerPanel;

    public NotebookIssuance() {
        // Создание фабрики сессий
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Notebook.class)
                .buildSessionFactory();
        setSize(WIDTH, HEIGHT/2);
        setLocation(100, 250);
        setResizable(false);
        setTitle("ВЫДАЧА НОУТБУКА. Заполните поля!");

        createPanelNotebook(sessionFactory);
        setVisible(true);
    }
    private void createPanelNotebook(SessionFactory sessionFactory) {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(btnAddNotebook = new JButton("Запись информации"), BorderLayout.SOUTH);

        btnAddNotebook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (true) {
                    System.out.println(textSum);
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Notebook notebook = new Notebook(studentLastName.getText(),groupNumber.getText(),
                            Integer.parseInt(notebookNumber.getText()), timestamp);
                    saveInLog(String.valueOf(notebook));
                    System.out.println(notebook);
// Создание сессии
                    Session session = sessionFactory.getCurrentSession();

                    try {
                        // Начало транзакции
                        session.beginTransaction();
                        session.save(notebook);
                        System.out.println("Object notebook save successfully");
                        Notebook retrievedNotebook = session.get(Notebook.class, notebook.getId());
                        System.out.println("Object notebook retrieved successfully");
                        System.out.println("Retrieved notebook object: " + retrievedNotebook);
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
        notebookNumber = new JTextField("НОМЕР НОУТБУКА");
        notebookNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n'){
                    try {
                        Integer.parseInt(notebookNumber.getText());
                        notebookNumber.setForeground(Color.GREEN);
                    }catch (NumberFormatException ex) {
                        System.out.println("Введите число");

                    }
                    textSum= textSum + notebookNumber.getText();
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
        headerPanel.add(notebookNumber);
        headerPanel.add(new JPanel());
        headerPanel.add(studentLastName);
        headerPanel.add(groupNumber);

        return headerPanel;
    }
    public void writing () {
        System.out.println("Ноутбук добавлен к выданным!");
    }



    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING){

        }
    }
private void closing (SessionFactory sessionFactory){
    sessionFactory.close();
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
