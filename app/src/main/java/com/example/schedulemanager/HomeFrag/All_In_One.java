package com.example.schedulemanager.HomeFrag;

import com.example.schedulemanager.Task.Task;
import com.example.schedulemanager.email.Email;

public class All_In_One {

    public Email emailItem;
    public Task taskItem;
//    public Notes notesItem;

    public All_In_One() {
    }

    public All_In_One(Email emailItem, Task taskItem) {
        this.emailItem = emailItem;
        this.taskItem = taskItem;
//        this.notesItem = notesItem;
    }

    public All_In_One(Email emailItem) {
        this.emailItem = emailItem;
    }

    public All_In_One(Task taskItem) {
        this.taskItem = taskItem;
    }

//    public All_In_One(Notes notesItem) {
//        this.notesItem = notesItem;
//    }

    public Email getEmailItem() {
        return emailItem;
    }

    public void setEmailItem(Email emailItem) {
        this.emailItem = emailItem;
    }

    public Task getTaskItem() {
        return taskItem;
    }

    public void setTaskItem(Task taskItem) {
        this.taskItem = taskItem;
    }

//    public Notes getNotesItem() {
//        return notesItem;
//    }

//    public void setNotesItem(Notes notesItem) {
//        this.notesItem = notesItem;
//    }
}
