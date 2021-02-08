package com.t3leem_live.models;

import java.io.Serializable;

public class StreamModel implements Serializable {
    private int id;
    private String stage_id;
    private String class_id;
    private String department_id;
    private String subject_id;
    private String teacher_id;
    private String live_status;
    private String uuid;
    private String zoom_id;
    private String test_id;
    private String teacher_group_id;
    private String host_email;
    private String host_id;
    private String topic;
    private String agenda;
    private double teacher_live_price;
    private String start_url;
    private String join_url;
    private String duration;
    private String password;
    private String h323_password;
    private String pstn_password;
    private String encrypted_password;
    private String type;
    private TeacherModel teacher_fk;
    private StageClassModel stage_fk;
    private StageClassModel class_fk;
    private StageClassModel department_fk;
    private StageClassModel subject_fk;
    private TeacherModel user_stream_fk;

    public int getId() {
        return id;
    }

    public String getStage_id() {
        return stage_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public String getLive_status() {
        return live_status;
    }

    public String getUuid() {
        return uuid;
    }

    public String getZoom_id() {
        return zoom_id;
    }

    public String getTest_id() {
        return test_id;
    }

    public String getTeacher_group_id() {
        return teacher_group_id;
    }

    public String getHost_email() {
        return host_email;
    }

    public String getHost_id() {
        return host_id;
    }

    public String getTopic() {
        return topic;
    }

    public String getAgenda() {
        return agenda;
    }

    public double getTeacher_live_price() {
        return teacher_live_price;
    }

    public String getStart_url() {
        return start_url;
    }

    public String getJoin_url() {
        return join_url;
    }

    public String getDuration() {
        return duration;
    }

    public String getPassword() {
        return password;
    }

    public String getH323_password() {
        return h323_password;
    }

    public String getPstn_password() {
        return pstn_password;
    }

    public String getEncrypted_password() {
        return encrypted_password;
    }

    public String getType() {
        return type;
    }

    public TeacherModel getTeacher_fk() {
        return teacher_fk;
    }

    public StageClassModel getStage_fk() {
        return stage_fk;
    }

    public StageClassModel getClass_fk() {
        return class_fk;
    }

    public StageClassModel getDepartment_fk() {
        return department_fk;
    }

    public StageClassModel getSubject_fk() {
        return subject_fk;
    }

    public TeacherModel getUser_stream_fk() {
        return user_stream_fk;
    }

    public void setUser_stream_fk(TeacherModel user_stream_fk) {
        this.user_stream_fk = user_stream_fk;
    }
}
