package com.t3leem_live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.uis.module_teacher.activity_teacher_exams.TeacherExamsActivity;
import com.t3leem_live.databinding.TeacherExamRowBinding;
import com.t3leem_live.models.TeacherExamModel;

import java.util.List;

import io.paperdb.Paper;

public class TeacherExamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TeacherExamModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private TeacherExamsActivity activity;

    public TeacherExamsAdapter(List<TeacherExamModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang","ar");
        activity = (TeacherExamsActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        TeacherExamRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.teacher_exam_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        TeacherExamModel model = list.get(position);
        myHolder.binding.setModel(model);
        myHolder.binding.llShowResults.setOnClickListener(view -> {
            TeacherExamModel model2 = list.get(position);
            activity.setItemData(model2);
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public TeacherExamRowBinding binding;

        public MyHolder(@NonNull TeacherExamRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }



}
