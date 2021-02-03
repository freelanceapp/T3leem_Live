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
import com.t3leem_live.databinding.LoadMoreRowBinding;
import com.t3leem_live.databinding.TeacherRow3Binding;
import com.t3leem_live.databinding.TeacherRowBinding;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.uis.module_student.activity_available_teacher.AvailableTeacherActivity;
import com.t3leem_live.uis.module_student.activity_follow_teacher.FollowTeacherActivity;
import com.t3leem_live.uis.module_student.activity_student_teachers.StudentTeachersActivity;

import java.util.List;

public class StudentTeachersAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TeacherModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;


    public StudentTeachersAdapter2(List<TeacherModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TeacherRow3Binding binding = DataBindingUtil.inflate(inflater, R.layout.teacher_row3, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            TeacherModel model = list.get(position);
            myHolder.binding.setModel(model);

            myHolder.binding.llVideo.setOnClickListener(view -> {
                if (activity instanceof  FollowTeacherActivity){
                    FollowTeacherActivity followTeacherActivity = (FollowTeacherActivity) activity;
                    TeacherModel model2 = list.get(myHolder.getAdapterPosition());
                    followTeacherActivity.setTeacherItem(model2);
                }else if (activity instanceof AvailableTeacherActivity){
                    AvailableTeacherActivity availableTeacherActivity = (AvailableTeacherActivity) activity;
                    TeacherModel model2 = list.get(myHolder.getAdapterPosition());
                    availableTeacherActivity.setTeacherItem(model2);
                }

            });

            myHolder.binding.llFollow.setOnClickListener(view -> {
                TeacherModel model2 = list.get(myHolder.getAdapterPosition());




                if (activity instanceof  FollowTeacherActivity){
                    FollowTeacherActivity followTeacherActivity = (FollowTeacherActivity) activity;
                    followTeacherActivity.follow_un_follow(model2, myHolder.getAdapterPosition());

                }else if (activity instanceof AvailableTeacherActivity){

                    boolean type = false;

                    if (model2.getFollow_fk() == null) {
                        model2.setFollow_fk(new TeacherModel.FollowFk());
                        type = true;
                    } else {
                        model2.setFollow_fk(null);
                        type = false;

                    }
                    list.set(myHolder.getAdapterPosition(), model2);
                    notifyItemChanged(myHolder.getAdapterPosition());


                    AvailableTeacherActivity availableTeacherActivity = (AvailableTeacherActivity) activity;
                    availableTeacherActivity.follow_un_follow(model2, type, myHolder.getAdapterPosition());

                }

            });

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private TeacherRow3Binding binding;

        public MyHolder(TeacherRow3Binding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
