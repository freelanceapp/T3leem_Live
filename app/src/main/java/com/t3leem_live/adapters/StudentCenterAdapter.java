package com.t3leem_live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.databinding.StudentCenterRowBinding;
import com.t3leem_live.databinding.StudentExamRowBinding;
import com.t3leem_live.models.StudentCenterModel;
import com.t3leem_live.models.TeacherExamModel;
import com.t3leem_live.uis.module_student.activity_student_center.StudentCenterActivity;
import com.t3leem_live.uis.module_student.activity_student_exam.StudentExamActivity;

import java.util.List;

import io.paperdb.Paper;

public class StudentCenterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StudentCenterModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private StudentCenterActivity activity;

    public StudentCenterAdapter(List<StudentCenterModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang","ar");
        activity = (StudentCenterActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        StudentCenterRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.student_center_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        StudentCenterModel model = list.get(position);
        myHolder.binding.setModel(model);
        myHolder.binding.llGroup.setOnClickListener(view -> {
            StudentCenterModel model2 = list.get(position);
            activity.setItemData(model2,"group");

        });

        myHolder.binding.llShare.setOnClickListener(view -> {
            StudentCenterModel model2 = list.get(position);
            activity.setItemData(model2,"share");

        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public StudentCenterRowBinding binding;

        public MyHolder(@NonNull StudentCenterRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }



}
