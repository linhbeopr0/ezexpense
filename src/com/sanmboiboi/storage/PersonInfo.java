package com.sanmboiboi.storage;

public class PersonInfo {
    String name, path;
    double balance;
    int id;
    

    public PersonInfo() {
        this.name = "";
        this.path = "";
        this.balance = 0;
        this.id = 0;
    }

    public PersonInfo(int id, String name, double balance, String path) {
        this.name = name;
        this.path = path;
        this.balance = balance;
        this.id = id;
    }

    public PersonInfo(String name, double balance, String path) {
        this.name = name;
        this.path = path;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
