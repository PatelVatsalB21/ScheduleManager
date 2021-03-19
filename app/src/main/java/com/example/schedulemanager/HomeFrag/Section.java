package com.example.schedulemanager.HomeFrag;

import java.util.ArrayList;

public class Section {

    public String Head;
    public ArrayList<All_In_One> items = new ArrayList<>();

    public String getHead() {
        return Head;
    }

    public ArrayList<All_In_One> getItems() {
        return items;
    }

    public Section() {
    }
}
