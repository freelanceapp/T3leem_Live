package com.t3leem_live.models;

import java.io.Serializable;
import java.util.List;

public class TeacherStudentsDataModel implements Serializable {
    private int current_page;
    private List<TeacherStudentsModel> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<TeacherStudentsModel> getData() {
        return data;
    }
}
