package com.t3leem_live.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.databinding.CenterGroupRow2Binding;
import com.t3leem_live.databinding.TeacherRow2Binding;
import com.t3leem_live.models.CenterGroupModel;
import com.t3leem_live.models.GroupOfTeacher;
import com.t3leem_live.models.StudentSelectedGroup;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.models.TeachersInsideCenterModel;
import com.t3leem_live.uis.module_student.activity_student_center_groups.StudentCenterGroupsActivity;
import com.t3leem_live.uis.module_student.activity_student_teachers_group.StudentTeachersGroupActivity;
import com.t3leem_live.uis.module_student.activity_teachers_inside_center.TeachersInsideCenterActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_video.TeacherVideoActivity;

import java.util.ArrayList;
import java.util.List;

public class TeacherInsideCenterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TeachersInsideCenterModel> list;
    private Context context;
    private LayoutInflater inflater;
    private TeachersInsideCenterActivity activity;
    public TeacherInsideCenterAdapter(List<TeachersInsideCenterModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (TeachersInsideCenterActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        TeacherRow2Binding binding = DataBindingUtil.inflate(inflater, R.layout.teacher_row2, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        TeachersInsideCenterModel model = list.get(position);
        myHolder.binding.setModel(model);
        GroupOfTeacher groupOfTeacher = new GroupOfTeacher(0,context.getString(R.string.ch_time));
        List<GroupOfTeacher> groupOfTeacherList = new ArrayList<>();
        groupOfTeacherList.add(groupOfTeacher);
        groupOfTeacherList.addAll(model.getTeacher_fk().getTeacher_groups_fk());
        SpinnerGroupOfTeacherAdapter adapter = new SpinnerGroupOfTeacherAdapter(groupOfTeacherList,context);
        myHolder.binding.spinner.setAdapter(adapter);
        myHolder.binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                GroupOfTeacher groupOfTeacher2 = groupOfTeacherList.get(i);
                StudentSelectedGroup selectedGroup;
                if (i!=0){
                    selectedGroup = new StudentSelectedGroup(model.getId(),groupOfTeacher2.getId());
                }else {
                    selectedGroup = new StudentSelectedGroup(model.getId(),0);

                }

                activity.setSelectedGroup(selectedGroup);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        myHolder.binding.llVideo.setOnClickListener(view -> {

            TeachersInsideCenterModel model2 = list.get(myHolder.getAdapterPosition());

            activity.setTeacherItem(model2.getTeacher_fk());
        });

        myHolder.binding.llShare.setOnClickListener(view -> {

            TeachersInsideCenterModel model2 = list.get(myHolder.getAdapterPosition());
            activity.share(model2.getTeacher_fk());
        });


        myHolder.binding.llFollow.setOnClickListener(view -> {

            TeachersInsideCenterModel model2 = list.get(myHolder.getAdapterPosition());

            TeacherModel teacherModel = model2.getTeacher_fk();
            boolean type = false;

            if (teacherModel.getFollow_fk()==null){
                teacherModel.setFollow_fk(new TeacherModel.FollowFk());
                type = true;
            }else {
                teacherModel.setFollow_fk(null);
                type = false;

            }

            model2.setTeacher_fk(teacherModel);
            notifyItemChanged(myHolder.getAdapterPosition());
            activity.follow_un_follow(teacherModel,type,myHolder.getAdapterPosition());
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public TeacherRow2Binding binding;

        public MyHolder(@NonNull TeacherRow2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
