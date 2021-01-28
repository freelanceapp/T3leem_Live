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
import com.t3leem_live.databinding.CenterGroupRowBinding;
import com.t3leem_live.databinding.CenterTeacherGroupRowBinding;
import com.t3leem_live.models.CenterGroupModel;
import com.t3leem_live.models.CenterGroupTeacherModel;
import com.t3leem_live.uis.module_center_course.activity_center_group_details.CenterGroupDetailsActivity;
import com.t3leem_live.uis.module_center_course.activity_home_center.fragments.Fragment_Home_Center;

import java.util.List;

import io.paperdb.Paper;

public class CenterGroupsTeacherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CenterGroupTeacherModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public CenterGroupsTeacherAdapter(List<CenterGroupTeacherModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CenterTeacherGroupRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.center_teacher_group_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        CenterGroupTeacherModel model = list.get(position);
        myHolder.binding.setModel(model);
myHolder.binding.fldelete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(context instanceof CenterGroupDetailsActivity){
            CenterGroupDetailsActivity centerGroupDetailsActivity=(CenterGroupDetailsActivity)context;
            centerGroupDetailsActivity.deleteTeacher(myHolder.getLayoutPosition());
        }
    }
});

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CenterTeacherGroupRowBinding binding;

        public MyHolder(@NonNull CenterTeacherGroupRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
