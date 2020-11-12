package com.t3leem_live.models;

import java.io.Serializable;

public class TeacherExamModel implements Serializable {
    private int id;
    private String stage_id;
    private String class_id;
    private String department_id;
    private String subject_id;
    private String teacher_id;
    private String title;
    private String title_en;
    private String desc;
    private String exam_day;
    private String start_at;
    private String end_at;
    private String duration;
    private String choose_degree;
    private String true_false_degree;
    private String article_degree;
    private String total_degree;
    private String success_degree;
    private String edit_view_exam_link;
    private String enter_exam;
    private String correct_article;
    private TeacherModel teacher_fk;


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

    public String getTitle() {
        return title;
    }

    public String getTitle_en() {
        return title_en;
    }

    public String getDesc() {
        return desc;
    }

    public String getExam_day() {
        return exam_day;
    }

    public String getStart_at() {
        return start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public String getDuration() {
        return duration;
    }

    public String getChoose_degree() {
        return choose_degree;
    }

    public String getTrue_false_degree() {
        return true_false_degree;
    }

    public String getArticle_degree() {
        return article_degree;
    }

    public String getTotal_degree() {
        return total_degree;
    }

    public String getSuccess_degree() {
        return success_degree;
    }

    public String getEdit_view_exam_link() {
        return edit_view_exam_link;
    }

    public String getEnter_exam() {
        return enter_exam;
    }

    public String getCorrect_article() {
        return correct_article;
    }

    public TeacherModel getTeacher_fk() {
        return teacher_fk;
    }
}
