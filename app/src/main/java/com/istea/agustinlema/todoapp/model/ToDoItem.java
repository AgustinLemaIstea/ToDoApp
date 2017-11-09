package com.istea.agustinlema.todoapp.model;

public class ToDoItem {
    private int id;
    private String title;
    private String body;
    private boolean isImportant;

    public ToDoItem(int id) {
        this.id = id;
    }

    public ToDoItem(int id, String title, String body, boolean isImportant) {
        this(id);
        this.setTitle(title);
        this.setBody(body);
        this.setImportant(isImportant);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public int getId() {
        return id;
    }
}
