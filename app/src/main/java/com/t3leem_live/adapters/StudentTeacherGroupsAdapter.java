package com.t3leem_live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.uis.module_student.activity_student_teachers_group.StudentTeachersGroupActivity;
import com.t3leem_live.databinding.GroupRowBinding;
import com.t3leem_live.models.TeacherGroupModel;

import java.util.List;

import io.paperdb.Paper;

public class StudentTeacherGroupsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TeacherGroupModel> list;
    private Context context;
    private LayoutInflater inflater;
    private StudentTeachersGroupActivity activity;

    public StudentTeacherGroupsAdapter(List<TeacherGroupModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        activity = (StudentTeachersGroupActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        GroupRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.group_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        TeacherGroupModel model = list.get(position);
        myHolder.binding.setModel(model);
        myHolder.binding.tvReserve.setOnClickListener(view -> {
            TeacherGroupModel model2 = list.get(myHolder.getAdapterPosition());
            if (model2.getStudent_book()==null){
                activity.setItemData(model2,myHolder.getAdapterPosition());
            }

        });





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public GroupRowBinding binding;

        public MyHolder(@NonNull GroupRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }



}
