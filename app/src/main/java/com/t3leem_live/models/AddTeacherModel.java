package com.t3leem_live.models;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.t3leem_live.BR;
import com.t3leem_live.R;

public class AddTeacherModel extends BaseObservable {
    private String name;
    private String stage;
    private String teacher_price;
    private String discount_value;
    private String teacher_price_after_discount;
    private String admin_value;
    private String final_price;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_stage = new ObservableField<>();
    public ObservableField<String> error_teacher_price = new ObservableField<>();
    public ObservableField<String> error_admin_value = new ObservableField<>();
    public ObservableField<String> error_final_price = new ObservableField<>();
    public ObservableField<String> error_price_after_discount = new ObservableField<>();
    public ObservableField<String> error_discount_value = new ObservableField<>();

    public boolean isDataValid(Context context) {


        if (!name.isEmpty() &&
                !stage.isEmpty() &&
                !teacher_price.isEmpty() &&
                !discount_value.isEmpty() &&

                !teacher_price_after_discount.isEmpty()
                &&

                !admin_value.isEmpty()
                &&

                !final_price.isEmpty()


        ) {


            error_stage.set(null);
            error_name.set(null);
            error_admin_value.set(null);
            error_final_price.set(null);
            error_teacher_price.set(null);
            error_discount_value.set(null);
            error_price_after_discount.set(null);
            if(Double.parseDouble(teacher_price_after_discount)<=0||Double.parseDouble(final_price)<=0){
            if(Double.parseDouble(final_price)<=0){
                error_final_price.set(context.getResources().getString(R.string.must_big));
            }
            if(Double.parseDouble(teacher_price_after_discount)<=0){
                error_price_after_discount.set(context.getResources().getString(R.string.must_big));

            }
            return false;
            }
            return true;
        } else {

            if (name.trim().isEmpty()) {
              //  error_name.set(context.getString(R.string.field_required));
                Toast.makeText(context,context.getResources().getString(R.string.select_teacher),Toast.LENGTH_LONG).show();
            } else {
                error_name.set(null);

            }

            if (stage.trim().isEmpty()) {
              //  error_stage.set(context.getString(R.string.field_required));
                Toast.makeText(context,context.getResources().getString(R.string.select_stage),Toast.LENGTH_LONG).show();

            } else {
                error_stage.set(null);

            }

            if (teacher_price.trim().isEmpty()) {
                error_teacher_price.set(context.getString(R.string.field_required));
            } else {
                error_teacher_price.set(null);

            }

            if (discount_value.isEmpty()) {
                error_discount_value.set(context.getString(R.string.field_required));
            } else {
                error_discount_value.set(null);

            }


            if (teacher_price_after_discount.trim().isEmpty()) {
                error_price_after_discount.set(context.getString(R.string.field_required));
            } else {
                error_price_after_discount.set(null);

            }

            if (admin_value.trim().isEmpty()) {
                error_admin_value.set(context.getString(R.string.field_required));
            } else {
                error_admin_value.set(null);

            }
            if (final_price.trim().isEmpty()) {
                error_final_price.set(context.getString(R.string.field_required));
            } else {
                error_final_price.set(null);

            }
            return false;

        }

    }

    public AddTeacherModel() {
        name = "";
        stage = "";
        teacher_price = "";
        discount_value = "";
        teacher_price_after_discount = "";
        admin_value = "";
        final_price = "";

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
    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
        notifyPropertyChanged(BR.stage);
    }

    @Bindable

    public String getTeacher_price() {
        return teacher_price;
    }

    public void setTeacher_price(String teacher_price) {
        this.teacher_price = teacher_price;
        notifyPropertyChanged(BR.teacher_price);
    }

    @Bindable

    public String getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(String discount_value) {
        this.discount_value = discount_value;
        notifyPropertyChanged(BR.discount_value);
    }

    @Bindable

    public String getTeacher_price_after_discount() {
        return teacher_price_after_discount;
    }

    public void setTeacher_price_after_discount(String teacher_price_after_discount) {
        this.teacher_price_after_discount = teacher_price_after_discount;
        notifyPropertyChanged(BR.teacher_price_after_discount);
    }

    @Bindable

    public String getAdmin_value() {
        return admin_value;
    }

    public void setAdmin_value(String admin_value) {
        this.admin_value = admin_value;
        notifyPropertyChanged(BR.admin_value);
    }

    @Bindable

    public String getFinal_price() {
        return final_price;
    }

    public void setFinal_price(String final_price) {
        this.final_price = final_price;

        notifyPropertyChanged(BR.final_price);
    }

}
