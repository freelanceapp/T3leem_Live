package com.t3leem_live.models;

import java.io.Serializable;

public class StudentSelectedGroup implements Serializable {
    private int id;
    private int group_id;

    public StudentSelectedGroup(int id, int group_id) {
        this.id = id;
        this.group_id = group_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }
}
