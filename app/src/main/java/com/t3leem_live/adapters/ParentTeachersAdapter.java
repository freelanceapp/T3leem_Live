package com.t3leem_live.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.databinding.LoadMoreRowBinding;
import com.t3leem_live.databinding.TeacherRow3Binding;
import com.t3leem_live.databinding.TeacherRow4Binding;
import com.t3leem_live.databinding.TeacherRowBinding;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.uis.module_parent.activity_son_teacher.ParentSonTeacherActivity;
import com.t3leem_live.uis.module_student.activity_student_teachers.StudentTeachersActivity;

import java.util.List;

public class ParentTeachersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DATA = 1;
    private final int LOAD = 2;
    private List<TeacherModel> list;
    private Context context;
    private LayoutInflater inflater;
    private ParentSonTeacherActivity activity;


    public ParentTeachersAdapter(List<TeacherModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (ParentSonTeacherActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==DATA){
            TeacherRow4Binding binding = DataBindingUtil.inflate(inflater, R.layout.teacher_row4, parent, false);
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
            TeacherModel model= list.get(position);
            myHolder.binding.setModel(model);
            myHolder.binding.llVideo.setOnClickListener(v -> {
                TeacherModel model2= list.get(myHolder.getAdapterPosition());
                activity.setTeacherItem(model2);

            });


            myHolder.binding.llCreateRoom.setOnClickListener(v -> {
                TeacherModel model2= list.get(myHolder.getAdapterPosition());
                activity.createRoom(model2);

            });


        }else if (holder instanceof LoadMoreHolder){
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.prgBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            loadMoreHolder.binding.prgBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private TeacherRow4Binding binding;

        public MyHolder(TeacherRow4Binding binding) {
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
