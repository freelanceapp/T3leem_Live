package com.t3leem_live.models;

import java.io.Serializable;
import java.util.List;

public class TeachersDataModel implements Serializable {
    private int current_page;
    private List<TeacherModel> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<TeacherModel> getData() {
        return data;
    }
}
