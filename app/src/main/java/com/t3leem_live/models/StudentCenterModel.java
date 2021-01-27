package com.t3leem_live.models;

import java.io.Serializable;

public class StudentCenterModel implements Serializable {
    private int id;
    private String email;
    private String name;
    private String code;
    private String user_type;
    private String is_parent;
    private int student_money;
    private String phone_code;
    private String phone;
    private String parent_phone;
    private String parent;
    private int number_of_groups;
    private int stage_id;
    private String class_id;
    private String department_id;
    private String subject_id;
    private String zoom_id;
    private String live_status;
    private int teacher_live_price;
    private String teacher_degree;
    private String teacher_video;
    private int teacher_ratio;
    private int rates;
    private String google;
    private String facebook;
    private String twitter;
    private String school_name;
    private String address;
    private int latitude;
    private int longitude;
    private String logo;
    private String banner;
    private String email_verified_at;
    private String added_by;
    private String address_1;
    private String address_2;
    private String state;
    private String city;
    private String postcode;
    private String is_blocked;
    private String is_login;
    private String logout_time;
    private int is_confirmed;
    private String confirmation_code;
    private String forget_password_code;
    private String software_type;
    private String deleted_at;
    private StageClassModel stage_fk;


    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getIs_parent() {
        return is_parent;
    }

    public int getStudent_money() {
        return student_money;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getParent_phone() {
        return parent_phone;
    }

    public String getParent() {
        return parent;
    }

    public int getNumber_of_groups() {
        return number_of_groups;
    }

    public int getStage_id() {
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

    public String getZoom_id() {
        return zoom_id;
    }

    public String getLive_status() {
        return live_status;
    }

    public int getTeacher_live_price() {
        return teacher_live_price;
    }

    public String getTeacher_degree() {
        return teacher_degree;
    }

    public String getTeacher_video() {
        return teacher_video;
    }

    public int getTeacher_ratio() {
        return teacher_ratio;
    }

    public int getRates() {
        return rates;
    }

    public String getGoogle() {
        return google;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getSchool_name() {
        return school_name;
    }

    public String getAddress() {
        return address;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public String getLogo() {
        return logo;
    }

    public String getBanner() {
        return banner;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public String getAdded_by() {
        return added_by;
    }

    public String getAddress_1() {
        return address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getIs_blocked() {
        return is_blocked;
    }

    public String getIs_login() {
        return is_login;
    }

    public String getLogout_time() {
        return logout_time;
    }

    public int getIs_confirmed() {
        return is_confirmed;
    }

    public String getConfirmation_code() {
        return confirmation_code;
    }

    public String getForget_password_code() {
        return forget_password_code;
    }

    public String getSoftware_type() {
        return software_type;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public StageClassModel getStage_fk() {
        return stage_fk;
    }
}
