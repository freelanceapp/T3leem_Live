package com.t3leem_live.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.databinding.CenterGroupRow2Binding;
import com.t3leem_live.databinding.CenterGroupRowBinding;
import com.t3leem_live.models.CenterGroupModel;
import com.t3leem_live.uis.module_center_course.activity_home_center.fragments.Fragment_Home_Center;
import com.t3leem_live.uis.module_student.activity_student_center_groups.StudentCenterGroupsActivity;

import java.util.List;

import io.paperdb.Paper;

public class StudentCenterGroupsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CenterGroupModel> list;
    private Context context;
    private LayoutInflater inflater;
    private StudentCenterGroupsActivity activity;

    public StudentCenterGroupsAdapter(List<CenterGroupModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (StudentCenterGroupsActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CenterGroupRow2Binding binding = DataBindingUtil.inflate(inflater, R.layout.center_group_row2, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        CenterGroupModel model = list.get(position);
        myHolder.binding.setModel(model);
        myHolder.binding.llTeacher.setOnClickListener(view -> {
            CenterGroupModel model2 = list.get(myHolder.getAdapterPosition());
            activity.setItemData(model2);

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CenterGroupRow2Binding binding;

        public MyHolder(@NonNull CenterGroupRow2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
