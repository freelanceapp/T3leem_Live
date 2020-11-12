package com.t3leem_live.models;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.t3leem_live.BR;
import com.t3leem_live.R;

import java.io.Serializable;

public class CreateRoomModel extends BaseObservable implements Serializable {
    private String title;
    private String subject;
    private String price;
    private String duration;

    public ObservableField<String> error_title = new ObservableField<>();
    public ObservableField<String> error_subject = new ObservableField<>();
    public ObservableField<String> error_price = new ObservableField<>();
    public ObservableField<String> error_duration = new ObservableField<>();


    public boolean isDataValid(Context context){
        if (!title.isEmpty()&&
                !subject.isEmpty()&&
                !price.isEmpty()&&
                !duration.isEmpty()

        ){
            error_title.set(null);
            error_subject.set(null);
            error_price.set(null);
            error_duration.set(null);
            return true;
        }else {
            if (title.isEmpty()){
                error_title.set(context.getString(R.string.field_required));
            }else {
                error_title.set(null);
            }
            if (subject.isEmpty()){
                error_subject.set(context.getString(R.string.field_required));
            }else {
                error_subject.set(null);
            }
            if (duration.isEmpty()){
                error_duration.set(context.getString(R.string.field_required));
            }else {
                error_duration.set(null);
            }
            if (price.isEmpty()){
                error_price.set(context.getString(R.string.field_required));
            }else {
                error_price.set(null);
            }
            return false;
        }
    }

    public CreateRoomModel() {
        title = "";
        subject = "";
        price = "";
        duration ="";
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
        notifyPropertyChanged(BR.subject);

    }
    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);

    }
    @Bindable
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
        notifyPropertyChanged(BR.duration);

    }
}
