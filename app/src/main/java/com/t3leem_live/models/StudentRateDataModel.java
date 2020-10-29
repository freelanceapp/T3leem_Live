package com.t3leem_live.models;

import java.io.Serializable;
import java.util.List;

public class StudentRateDataModel implements Serializable {
    private List<StudentRateModel> data;

    public List<StudentRateModel> getData() {
        return data;
    }
}
