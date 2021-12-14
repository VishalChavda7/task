package com.vishalchavda.myapplication.Models;

public class Taskmodel {

    String task,id;

    public Taskmodel() {
    }

    public Taskmodel(String task, String id) {
        this.task = task;
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
