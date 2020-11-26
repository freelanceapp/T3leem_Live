package com.t3leem_live.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.uis.module_teacher.activity_teacher_students.TeacherStudentsActivity;
import com.t3leem_live.databinding.LoadMoreRowBinding;
import com.t3leem_live.databinding.StudentRowBinding;
import com.t3leem_live.models.TeacherStudentsModel;

import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DATA = 1;
    private final int LOAD = 2;
    private List<TeacherStudentsModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;


    public StudentsAdapter(List<TeacherStudentsModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==DATA){
            StudentRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.student_row, parent, false);
            return new MyHolder(binding);
        }else {
            LoadMoreRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.load_more_row, parent, false);
            return new LoadMoreHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder){
            MyHolder myHolder = (MyHolder) holder;
            TeacherStudentsModel model= list.get(position);
            myHolder.binding.setModel(model);
            myHolder.itemView.setOnClickListener(view ->{
                if (activity instanceof TeacherStudentsActivity){
                    TeacherStudentsModel model2= list.get(holder.getAdapterPosition());

                    TeacherStudentsActivity teacherStudentsActivity = (TeacherStudentsActivity) activity;
                    teacherStudentsActivity.navigateToStudentProfileActivity(model2);
                }
            });


        }else if (holder instanceof LoadMoreHolder){
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.prgBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.color1), PorterDuff.Mode.SRC_IN);
            loadMoreHolder.binding.prgBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private StudentRowBinding binding;

        public MyHolder(StudentRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    public static class LoadMoreHolder extends RecyclerView.ViewHolder {
        private LoadMoreRowBinding binding;

        public LoadMoreHolder(LoadMoreRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position)==null){
            return LOAD;
        }else {
            return DATA;
        }
    }
}
