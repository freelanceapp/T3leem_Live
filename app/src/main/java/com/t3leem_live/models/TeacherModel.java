package com.t3leem_live.models;

import java.io.Serializable;
import java.util.List;

public class TeacherModel implements Serializable {
    private int id;
    private String email;
    private String name;
    private String code;
    private String user_type;
    private String phone_code;
    private String phone;
    private String stage_id;
    private String class_id;
    private String department_id;
    private String subject_id;
    private String parent_phone;
    private String teacher_degree;
    private String teacher_video;
    private int number_of_groups;
    private String logo;
    private double rates;
    private StageClassModel stage_fk;
    private StageClassModel class_fk;
    private StageClassModel department_fk;
    private List<StudentRateModel> student_rates;
    private List<GroupOfTeacher> teacher_groups_fk;
    private FollowFk follow_fk;
    private RoomModel room_user_fk;


    public TeacherModel() {
    }

    public TeacherModel(int id, String email, String name, String code, String user_type, String phone_code, String phone, String stage_id, String class_id, String department_id, String subject_id, String parent_phone, String teacher_degree, String teacher_video, String logo, StageClassModel stage_fk, StageClassModel class_fk, StageClassModel department_fk, List<StudentRateModel> student_rates) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.code = code;
        this.user_type = user_type;
        this.phone_code = phone_code;
        this.phone = phone;
        this.stage_id = stage_id;
        this.class_id = class_id;
        this.department_id = department_id;
        this.subject_id = subject_id;
        this.parent_phone = parent_phone;
        this.teacher_degree = teacher_degree;
        this.teacher_video = teacher_video;
        this.logo = logo;
        this.stage_fk = stage_fk;
        this.class_fk = class_fk;
        this.department_fk = department_fk;
        this.student_rates = student_rates;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getParent_phone() {
        return parent_phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStage_id() {
        return stage_id;
    }

    public void setStage_id(String stage_id) {
        this.stage_id = stage_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getTeacher_degree() {
        return teacher_degree;
    }

    public void setTeacher_degree(String teacher_degree) {
        this.teacher_degree = teacher_degree;
    }

    public String getTeacher_video() {
        return teacher_video;
    }

    public void setTeacher_video(String teacher_video) {
        this.teacher_video = teacher_video;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public FollowFk getFollow_fk() {
        return follow_fk;
    }

    public StageClassModel getStage_fk() {
        return stage_fk;
    }

    public void setStage_fk(StageClassModel stage_fk) {
        this.stage_fk = stage_fk;
    }

    public StageClassModel getClass_fk() {
        return class_fk;
    }

    public void setClass_fk(StageClassModel class_fk) {
        this.class_fk = class_fk;
    }

    public StageClassModel getDepartment_fk() {
        return department_fk;
    }

    public void setDepartment_fk(StageClassModel department_fk) {
        this.department_fk = department_fk;
    }

    public double getRates() {
        return rates;
    }

    public List<StudentRateModel> getStudent_rates() {
        return student_rates;
    }

    public List<GroupOfTeacher> getTeacher_groups_fk() {
        return teacher_groups_fk;
    }

    public int getNumber_of_groups() {
        return number_of_groups;
    }

    public static class FollowFk implements Serializable {

    }

    public void setFollow_fk(FollowFk follow_fk) {
        this.follow_fk = follow_fk;
    }

    public RoomModel getRoom_user_fk() {
        return room_user_fk;
    }
}
