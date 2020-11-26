package com.t3leem_live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.t3leem_live.R;
import com.t3leem_live.uis.module_general.activity_login.LoginActivity;
import com.t3leem_live.uis.module_student.activity_student_sign_up.StudentSignUpActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_sign_up.TeacherSignUpActivity;
import com.t3leem_live.databinding.CountriesRowBinding;
import com.t3leem_live.models.CountryModel;

import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CountryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;
    public CountriesAdapter(List<CountryModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CountriesRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.countries_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));

        myHolder.itemView.setOnClickListener(view -> {
            if (activity instanceof LoginActivity){
                LoginActivity loginActivity = (LoginActivity) activity;
                loginActivity.setItemData(list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof StudentSignUpActivity){
                StudentSignUpActivity studentSignUpActivity = (StudentSignUpActivity) activity;
                studentSignUpActivity.setItemData(list.get(myHolder.getAdapterPosition()));

            }else if (activity instanceof TeacherSignUpActivity){
                TeacherSignUpActivity teacherSignUpActivity = (TeacherSignUpActivity) activity;
                teacherSignUpActivity.setItemData(list.get(myHolder.getAdapterPosition()));

            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public CountriesRowBinding binding;

        public MyHolder(@NonNull CountriesRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
