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
        private String fireBaseToken;
        private StageClassModel stage_fk;
        private StageClassModel class_fk;
        private StageClassModel department_fk;

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

        public void setFireBaseToken(String fireBaseToken) {
            this.fireBaseToken = fireBaseToken;
        }
    }
}
