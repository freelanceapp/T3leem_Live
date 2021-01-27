package com.t3leem_live.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.databinding.CenterGroupRowBinding;
import com.t3leem_live.databinding.GroupRowBinding;
import com.t3leem_live.models.CenterGroupModel;
import com.t3leem_live.models.TeacherGroupModel;
import com.t3leem_live.uis.module_student.activity_student_teachers_group.StudentTeachersGroupActivity;

import java.util.List;

import io.paperdb.Paper;

public class CenterGroupsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CenterGroupModel> list;
    private Context context;
    private LayoutInflater inflater;

    public CenterGroupsAdapter(List<CenterGroupModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CenterGroupRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.center_group_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        CenterGroupModel model = list.get(position);
        myHolder.binding.setModel(model);
        myHolder.binding.tvdetials.setPaintFlags(myHolder.binding.tvdetials.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);






    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CenterGroupRowBinding binding;

        public MyHolder(@NonNull CenterGroupRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }



}
