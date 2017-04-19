package com.example.smartalarm;

import java.io.Serializable;

/**
 * Created by Пользователь on 18.04.2017.
 */

public class ScrollElement implements Serializable {
    private String name;
    private boolean isDone;

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
