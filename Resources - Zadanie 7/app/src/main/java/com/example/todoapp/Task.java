package com.example.todoapp;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID id;
    private String name;
    private Date date;
    private boolean done;

    public Task() {
        id = UUID.randomUUID();
        date = new Date();
    }

    public Task(String _name, Boolean _done) {
        this();
        name = _name;
        done = _done;
    }

    public UUID getID() {
        return id;
    }

    public void setID(UUID _id) {
        id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date _date) {
        date = _date;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean _done) {
        done = _done;
    }
}
