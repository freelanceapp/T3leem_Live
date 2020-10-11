package com.t3leem_live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_student_home.fragments.Fragment_Library_Student;
import com.t3leem_live.databinding.LibraryRowBinding;
import com.t3leem_live.databinding.SummaryRowBinding;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.SummaryModel;

import java.util.List;

import io.paperdb.Paper;

public class SummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SummaryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;

    public SummaryAdapter(List<SummaryModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang","ar");


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        SummaryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.summary_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        SummaryModel summaryModel = list.get(position);
        myHolder.binding.setModel(summaryModel);






    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public SummaryRowBinding binding;

        public MyHolder(@NonNull SummaryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
