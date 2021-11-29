package com.example.todoapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static final TaskStorage taskStorage = new TaskStorage();

    private final List<Task> tasks;

    public static TaskStorage getInstance() { return taskStorage; }

    private TaskStorage() {
        tasks = new ArrayList<>();
        for (int i = 0; i<= 100; i++) {
            Task task = new Task();
            task.setName("Zadanie #" + i);
            task.setDone(i % 3 == 0);
            tasks.add(task);
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTask(UUID id) {

        for (Task t: tasks) {
            if(t.getID().equals(id))
                return t;
        }
        return tasks.get(0);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }
}
