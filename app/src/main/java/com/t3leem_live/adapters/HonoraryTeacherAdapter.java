package com.t3leem_live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.databinding.HonoraryTeacherRowBinding;
import com.t3leem_live.databinding.SummaryRowBinding;
import com.t3leem_live.models.SummaryModel;
import com.t3leem_live.models.TeacherModel;

import java.util.List;

import io.paperdb.Paper;

public class HonoraryTeacherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TeacherModel> list;
    private Context context;
    private LayoutInflater inflater;

    public HonoraryTeacherAdapter(List<TeacherModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);



    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        HonoraryTeacherRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.honorary_teacher_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        TeacherModel model = list.get(position);
        myHolder.binding.setModel(model);






    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public HonoraryTeacherRowBinding binding;

        public MyHolder(@NonNull HonoraryTeacherRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
