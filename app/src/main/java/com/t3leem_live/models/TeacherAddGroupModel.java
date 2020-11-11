package com.t3leem_live.models;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.t3leem_live.BR;
import com.t3leem_live.R;

public class TeacherAddGroupModel extends BaseObservable {
    private int subject_id;
    private int class_id;
    private String department_id;
    private String name;
    private String max_number;
    private String time;
    private boolean isHasDepartment;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_max_number = new ObservableField<>();
    public ObservableField<String> error_time = new ObservableField<>();

    public boolean isDataValid(Context context) {

        if (!name.isEmpty() &&
                !max_number.isEmpty() &&
                !time.isEmpty() &&
                subject_id!=0&&
                class_id!=0

        ) {


            error_name.set(null);
            error_max_number.set(null);
            error_time.set(null);

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

            if (name.trim().isEmpty()){
                error_name.set(context.getString(R.string.field_required));
            }else {
                error_name.set(null);

            }

            if (max_number.isEmpty()){
                error_max_number.set(context.getString(R.string.field_required));
            }else {
                error_max_number.set(null);

            }

            if (time.isEmpty()){
                error_time.set(context.getString(R.string.field_required));
            }else {
                error_time.set(null);

            }

            if (subject_id==0){
                Toast.makeText(context, R.string.choose_subject, Toast.LENGTH_SHORT).show();
            }

            if (class_id==0){
                Toast.makeText(context, R.string.choose_class, Toast.LENGTH_SHORT).show();
            }

            if (isHasDepartment){
                if (department_id.isEmpty()){
                    Toast.makeText(context, R.string.choose_department, Toast.LENGTH_SHORT).show();
                    return false;

                }
            }
            return false;

        }

    }

    public TeacherAddGroupModel() {
        name="";
        max_number = "";
        time = "";
        subject_id = 0;
        class_id=0;
        department_id = "";
        isHasDepartment = false;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }


    @Bindable
    public String getMax_number() {
        return max_number;
    }

    public void setMax_number(String max_number) {
        this.max_number = max_number;
        notifyPropertyChanged(BR.max_number);
    }

    @Bindable
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
    }

    public boolean isHasDepartment() {
        return isHasDepartment;
    }

    public void setHasDepartment(boolean hasDepartment) {
        isHasDepartment = hasDepartment;
    }
}
