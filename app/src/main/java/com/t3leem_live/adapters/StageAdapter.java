package com.t3leem_live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.uis.module_student.activity_student_home.fragments.Fragment_Home_Student;
import com.t3leem_live.databinding.StageRowBinding;
import com.t3leem_live.models.StageClassModel;

import java.util.List;

import io.paperdb.Paper;

public class StageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StageClassModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment_Home_Student fragment_home_student;
    private String lang;
    private int [] background = {R.drawable.gradient1,R.drawable.gradient2,R.drawable.gradient3,R.drawable.gradient4};

    public StageAdapter(List<StageClassModel> list, Context context, Fragment_Home_Student fragment_home_student) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment_home_student = fragment_home_student;
        Paper.init(context);
        lang = Paper.book().read("lang","ar");


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        StageRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.stage_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        StageClassModel classModel = list.get(position);
        myHolder.binding.setModel(classModel);
        int i = position%background.length;
        myHolder.binding.flContainer.setBackgroundResource(background[i]);

        myHolder.itemView.setOnClickListener(view -> {
            StageClassModel classModel2 = list.get(holder.getAdapterPosition());

            fragment_home_student.setItemData(classModel2);
        });





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public StageRowBinding binding;

        public MyHolder(@NonNull StageRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
