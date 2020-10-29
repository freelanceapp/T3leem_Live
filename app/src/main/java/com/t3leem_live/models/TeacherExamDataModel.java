package com.t3leem_live.models;

import java.io.Serializable;
import java.util.List;

public class TeacherExamDataModel implements Serializable {
    private List<TeacherExamModel> data;

    public List<TeacherExamModel> getData() {
        return data;
    }
}
