package com.t3leem_live.models;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.t3leem_live.BR;
import com.t3leem_live.R;

public class TeacherSignUpModel extends BaseObservable {
    private String imageUri;
    private String videoUri;
    private String name;
    private String phone_code;
    private String phone;
    private String email;
    private String password;
    private int stage_id;
    private int class_id;
    private String department_id;
    private String address;
    private String school_name;
    private boolean isHasDepartment;


    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_address = new ObservableField<>();
    public ObservableField<String> error_school_name = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!name.isEmpty() &&
                !phone_code.isEmpty() &&
                !phone.isEmpty() &&
                !email.isEmpty() &&
                password.length()>6 &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                stage_id!=0&&
                class_id!=0&&
                !address.isEmpty() &&
                !school_name.isEmpty()

        ) {
            error_phone.set(null);
            error_name.set(null);
            error_email.set(null);
            error_address.set(null);
            error_school_name.set(null);
            error_password.set(null);

            if (isHasDepartment){
                if (department_id.isEmpty()){
                    Toast.makeText(context, R.string.choose_department, Toast.LENGTH_SHORT).show();
                    return false;

                }else {
                    return true;

                }
            }else {
                return true;

            }
        } else {

            if (name.isEmpty()){
                error_name.set(context.getString(R.string.field_required));
            }else {
                error_name.set(null);

            }

            if (phone.isEmpty()){
                error_phone.set(context.getString(R.string.field_required));
            }else {
                error_phone.set(null);

            }

            if (email.isEmpty()){
                error_email.set(context.getString(R.string.field_required));
            }if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()){
                error_email.set(context.getString(R.string.inv_email));
            }else {
                error_email.set(null);

            }

            if (password.isEmpty()){
                error_password.set(context.getString(R.string.field_required));
            }else if (password.length()< 6){
                error_password.set(context.getString(R.string.password_short));

            }else {
                error_password.set(null);

            }


            if (stage_id==0){
                Toast.makeText(context, R.string.choose_stage, Toast.LENGTH_SHORT).show();
            }

            if (class_id==0){
                Toast.makeText(context, R.string.choose_class, Toast.LENGTH_SHORT).show();
            }

            if (address.isEmpty()){
                error_address.set(context.getString(R.string.field_required));
            }else {
                error_address.set(null);

            }

            if (school_name.isEmpty()){
                error_school_name.set(context.getString(R.string.field_required));
            }else {
                error_school_name.set(null);

            }
            return false;

        }

    }

    public TeacherSignUpModel() {
        imageUri = "";
        videoUri = "";
        name="";
        phone_code = "";
        phone = "";
        email = "";
        password="";
        stage_id = 0;
        department_id ="";
        address = "";
        school_name = "";
        isHasDepartment = false;

    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
        notifyPropertyChanged(BR.phone_code);

    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);

    }


    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getStage_id() {
        return stage_id;
    }

    public void setStage_id(int stage_id) {
        this.stage_id = stage_id;
    }





    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);

    }

    @Bindable
    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
        notifyPropertyChanged(BR.school_name);

    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public boolean isHasDepartment() {
        return isHasDepartment;
    }

    public void setHasDepartment(boolean hasDepartment) {
        isHasDepartment = hasDepartment;
    }
}
