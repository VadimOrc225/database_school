package ru.geekbrains.junior.diplom.models;

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
    public static final int WIDTH = 500;
    public static final int HEIGHT = 200;
    JButton btnAddNotebook;
    JTextField  studentLastName, notebookNumber, groupNumber;
    JTextArea log;
    JPanel headerPanel;

    public NotebookIssuance() {
        // Создание фабрики сессий
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Notebook.class)
                .buildSessionFactory();
        setSize(WIDTH, HEIGHT);
        setLocation(3, 250);
        setResizable(false);
        setTitle("ВЫДАЧА НОУТБУКА. Заполните поля!");

        createPanelNotebook(sessionFactory);
        setVisible(true);
    }
    private void createPanelNotebook(SessionFactory sessionFactory) {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(btnAddNotebook = new JButton("Запись информации"), BorderLayout.SOUTH);
        log = new JTextArea();
        log.setEditable(false);
        add(new JScrollPane(log));
        btnAddNotebook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (true) {
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
                        log.setText("");
                        session.save(notebook);
                        Notebook retrievedNotebook = session.get(Notebook.class, notebook.getId());

                        log.append("Записано: " + retrievedNotebook);
                        // Коммит транзакции
                        session.getTransaction().commit();
                    } finally {
                        session.close();
                    }
                    writing();
                    notebookNumber.setForeground(Color.BLACK);
                    groupNumber.setForeground(Color.BLACK);
                    studentLastName.setForeground(Color.BLACK);

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
                        notebookNumber.setForeground(Color.RED);
                        log.append("Введите число в поле НОМЕР!!!");

                    }
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
        String LOG_PATH = "./src/main/java/ru/geekbrains/junior/diplom/log.txt";
        try (FileWriter writer = new FileWriter(LOG_PATH, true)) {
            writer.write(text);
            writer.write("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
