package com.t3leem_live.models;

import java.io.Serializable;
import java.util.List;

public class TeacherGroupDataModel implements Serializable {
    private List<TeacherGroupModel> data;

    public List<TeacherGroupModel> getData() {
        return data;
    }
}
