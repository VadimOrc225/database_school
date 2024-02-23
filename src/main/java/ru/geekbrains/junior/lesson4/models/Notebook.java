package ru.geekbrains.junior.lesson4.models;

import javax.persistence.*;
import java.sql.Timestamp;
@Entity
@Table(name = "Notebooks")
public class Notebook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    private int number;
    private String lastname;
    private String group1;
    private int number;

    @Basic
    private Timestamp sqlTimestamp;

    public Notebook(String lastname, String group1, int number, Timestamp sqlTimestamp) {

        this.lastname = lastname;
        this.group1 = group1;
        this.number = number;
        this.sqlTimestamp = sqlTimestamp;

//        this.group = group;
//        this.dateTime = dateTime;
    }
    public Notebook(int id, String lastname, String group1, int number, Timestamp sqlTimestamp) {

        this.id = id;
        this.lastname = lastname;
        this.group1 = group1;
        this.number = number;
        this.sqlTimestamp = sqlTimestamp;
//        this.group = group;
//        this.dateTime = dateTime;
    }
    public Notebook(){

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public String getGroup1() {
        return group1;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setGroup1(String group1) {
        this.group1 = group1;
    }

//    public void setDateTime(Timestamp dateTime) {
//        this.dateTime = dateTime;
//    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Notebook {" +
                "number = " + number +
                ", взял " + lastname +
                " из группы " + group1 +
                "; " + sqlTimestamp + '}';
    }
}
