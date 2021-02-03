package com.t3leem_live.models;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    private int id;
    private String from_user_id;
    private String to_user_id;
    private String status;
    private String title;
    private String message;
    private String action_type;
    private String notification_type;
    private String exam_id;
    private TeacherModel from_user_fk;
    private String is_read;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public String getFrom_user_id() {
        return from_user_id;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getAction_type() {
        return action_type;
    }

    public String getIs_read() {
        return is_read;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public TeacherModel getFrom_user_fk() {
        return from_user_fk;
    }

    public String getExam_id() {
        return exam_id;
    }
}
