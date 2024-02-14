package ru.geekbrains.junior.lesson4.homework;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.geekbrains.junior.lesson4.models.Course;

import java.util.List;

public class DzLes4 {

    private static final int NUMBER = 1;  // номер курса в списке курсов

    public static void main(String[] args) {

        // Создание фабрики сессий
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Course.class)
                .buildSessionFactory();

        // Создание сессии
        Session session = sessionFactory.getCurrentSession();

        try {
            // Начало транзакции
            session.beginTransaction();

            // Создание объекта
            int c = NUMBER;   // курс под индексом NUMBER в списке курсов
            Course course = Course.create(c);
            session.save(course);
            System.out.println("Object course save successfully");
            Course retrievedCourse = session.get(Course.class, course.getId());
            System.out.println("Object course retrieved successfully");
            System.out.println("Retrieved course object: " + retrievedCourse);
            // Коммит транзакции
            session.getTransaction().commit();
        } finally {
            session.close();
        }

        // Создание сессии
        Session session1 = sessionFactory.getCurrentSession();
        // Чтение в список
        try {
            // Начало транзакции
            session1.beginTransaction();
            String readDataSQL = "SELECT * FROM courses;";
            List<Course> coursesList = (List<Course>) session1.createSQLQuery(readDataSQL).addEntity(Course.class).list();
            // List<Course> coursesList =  query.list();
            for (Course course1 : coursesList
            ) {
                System.out.println(course1);
            }

            // Коммит транзакции
            session1.getTransaction().commit();
        } finally {
            // Закрытие  сессии
            session1.close();
        }
//            // Обновление объекта
        Session session2 = sessionFactory.getCurrentSession();
        try {
            // Начало транзакции
            session2.beginTransaction();
            String readDataSQL = "SELECT * FROM courses;";
            List<Course> coursesList = (List<Course>) session2.createSQLQuery(readDataSQL).addEntity(Course.class).list();
            for (Course course1 : coursesList
            ) {
                course1.updateTitle();
                course1.updateDuration();
                System.out.println(course1);
            }
            System.out.println("All courses were updated successfully");
            // Коммит транзакции
            session2.getTransaction().commit();
        } finally {
            // Закрытие  сессии
            session2.close();
        }

        // Удаление данных
        Session session3 = sessionFactory.getCurrentSession();
        try {
            // Начало транзакции
            session3.beginTransaction();

            String readDataSQL = "DELETE FROM courses";
            session3.createSQLQuery(readDataSQL).executeUpdate();

            System.out.println("All courses were deleted from Courses successfully");

            // Коммит транзакции
            session3.getTransaction().commit();
        } finally {
            // Закрытие  сессии
            session3.close();
        }

        sessionFactory.close();


    }
}





