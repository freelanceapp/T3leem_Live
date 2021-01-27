package com.t3leem_live.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public static class User implements Serializable {
        private int id;
        private String code;
        private String name;
        private String email;
        private String is_parent;
        private double student_money;
        private int number_of_groups;
        private String live_status;
        private double teacher_live_price;
        private int teacher_ratio;
        private int rates;
        private String phone_code;
        private String phone;
        private String parent_phone;
        private String school_name;
        private String image;
        private String logo;
        private String token;
        private String stage_id;
        private String class_id;
        private String address;
        private String user_type;
        private String teacher_degree;
        private String teacher_video;
        private String fireBaseToken;
        private StageClassModel stage_fk;
        private StageClassModel class_fk;
        private StageClassModel department_fk;
        private List<StudentRateModel> student_rates;
        private User exist_son;

        public User() {
        }

        public int getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
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

        public String getSchool_name() {
            return school_name;
        }

        public String getImage() {
            return image;
        }

        public String getLogo() {
            return logo;
        }

        public String getToken() {
            return token;
        }

        public String getStage_id() {
            return stage_id;
        }

        public String getClass_id() {
            return class_id;
        }

        public String getAddress() {
            return address;
        }

        public String getUser_type() {
            return user_type;
        }

        public String getFireBaseToken() {
            return fireBaseToken;
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

        public String getTeacher_degree() {
            return teacher_degree;
        }

        public String getTeacher_video() {
            return teacher_video;
        }

        public void setFireBaseToken(String fireBaseToken) {
            this.fireBaseToken = fireBaseToken;
        }

        public List<StudentRateModel> getStudent_rates() {
            return student_rates;
        }

        public String getIs_parent() {
            return is_parent;
        }

        public double getStudent_money() {
            return student_money;
        }

        public int getNumber_of_groups() {
            return number_of_groups;
        }

        public String getLive_status() {
            return live_status;
        }

        public double getTeacher_live_price() {
            return teacher_live_price;
        }

        public int getTeacher_ratio() {
            return teacher_ratio;
        }

        public int getRates() {
            return rates;
        }

        public User getExist_son() {
            return exist_son;
        }
    }
}
