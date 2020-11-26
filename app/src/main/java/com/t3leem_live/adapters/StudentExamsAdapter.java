package com.t3leem_live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.uis.module_student.activity_student_exam.StudentExamActivity;
import com.t3leem_live.databinding.StudentExamRowBinding;
import com.t3leem_live.models.TeacherExamModel;

import java.util.List;

import io.paperdb.Paper;

public class StudentExamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TeacherExamModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private StudentExamActivity activity;

    public StudentExamsAdapter(List<TeacherExamModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang","ar");
        activity = (StudentExamActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        StudentExamRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.student_exam_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        TeacherExamModel model = list.get(position);
        myHolder.binding.setModel(model);
        myHolder.binding.llShow.setOnClickListener(view -> {
            TeacherExamModel model2 = list.get(position);
            activity.setItemData(model2);
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public StudentExamRowBinding binding;

        public MyHolder(@NonNull StudentExamRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }



}
