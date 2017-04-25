package com.example.smartalarm;

import java.io.Serializable;

/**
 * Created by Пользователь on 18.04.2017.
 */

public class ScrollElement implements Serializable {
    private String name;
    private int id = -1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
