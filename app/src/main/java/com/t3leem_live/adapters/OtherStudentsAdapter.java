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
import com.t3leem_live.databinding.OtherStudentRowBinding;
import com.t3leem_live.databinding.StudentRow2Binding;
import com.t3leem_live.databinding.StudentRowBinding;
import com.t3leem_live.models.TeacherStudentsModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.models.UsersDataModel;
import com.t3leem_live.uis.module_student.activity_other_students.OtherStudentsActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_students.TeacherStudentsActivity;

import java.util.List;

public class OtherStudentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserModel.User> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity activity;


    public OtherStudentsAdapter(List<UserModel.User> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        OtherStudentRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.other_student_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            UserModel.User model = list.get(position);
            myHolder.binding.setModel(model);

            if (model.isSelected()) {
                myHolder.binding.checkbox.setChecked(true);
            } else {
                myHolder.binding.checkbox.setChecked(false);
            }

            myHolder.binding.checkbox.setOnClickListener(view -> {
                UserModel.User model2 = list.get(myHolder.getAdapterPosition());
                if (myHolder.binding.checkbox.isChecked()) {
                    model2.setSelected(true);
                } else {
                    model2.setSelected(false);

                }
                list.set(myHolder.getAdapterPosition(),model2);

                notifyItemChanged(myHolder.getAdapterPosition());

                if (activity instanceof OtherStudentsActivity){
                    OtherStudentsActivity otherStudentsActivity = (OtherStudentsActivity) activity;
                    otherStudentsActivity.setItemData(model2);
                }

            });


        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private OtherStudentRowBinding binding;

        public MyHolder(OtherStudentRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
