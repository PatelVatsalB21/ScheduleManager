package com.example.schedulemanager.HomeFrag;

import com.example.schedulemanager.Task.Task;
import com.example.schedulemanager.email.Email;

public class All_In_One {

    public Email emailItem;
    public Task taskItem;

    public All_In_One(Email emailItem) {
        this.emailItem = emailItem;
    }

    public All_In_One(Task taskItem) {
        this.taskItem = taskItem;
    }
}
