package com.t3leem_live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.uis.module_teacher.activity_teacher_group.TeacherGroupActivity;
import com.t3leem_live.databinding.TeacherGroupRowBinding;
import com.t3leem_live.models.TeacherGroupModel;

import java.util.List;

import io.paperdb.Paper;

public class TeacherGroupsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TeacherGroupModel> list;
    private Context context;
    private LayoutInflater inflater;
    private TeacherGroupActivity activity;

    public TeacherGroupsAdapter(List<TeacherGroupModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        activity = (TeacherGroupActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        TeacherGroupRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.teacher_group_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        TeacherGroupModel model = list.get(position);
        myHolder.binding.setModel(model);
        myHolder.binding.llDelete.setOnClickListener(view -> {
            TeacherGroupModel model2 = list.get(myHolder.getAdapterPosition());
            activity.deleteGroup(model2,myHolder.getAdapterPosition());

        });
        myHolder.binding.llLive.setOnClickListener(view -> {
            TeacherGroupModel model2 = list.get(myHolder.getAdapterPosition());
            activity.startLiveStream(model2,myHolder.getAdapterPosition());
        });





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public TeacherGroupRowBinding binding;

        public MyHolder(@NonNull TeacherGroupRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }



}
