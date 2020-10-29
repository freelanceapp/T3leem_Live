package com.t3leem_live.models;

import java.io.Serializable;

public class StudentRateModel implements Serializable {
    private int id;
    private String subject_id;
    private String rate;
    private String from_total_rate;
    private String num_of_exam;
    private StageClassModel subject_fk;

    public int getId() {
        return id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public String getRate() {
        return rate;
    }

    public String getFrom_total_rate() {
        return from_total_rate;
    }

    public String getNum_of_exam() {
        return num_of_exam;
    }

    public StageClassModel getSubject_fk() {
        return subject_fk;
    }
}
