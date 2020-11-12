package com.t3leem_live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_student_home.fragments.Fragment_Live_Student;
import com.t3leem_live.databinding.StreamRowBinding;
import com.t3leem_live.databinding.SummaryRowBinding;
import com.t3leem_live.models.StreamModel;
import com.t3leem_live.models.SummaryModel;

import java.util.List;

import io.paperdb.Paper;

public class StreamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StreamModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private Fragment_Live_Student fragment;
    public StreamAdapter(List<StreamModel> list, Context context,Fragment_Live_Student fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang","ar");
        this.fragment = fragment;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        StreamRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.stream_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        StreamModel streamModel = list.get(position);
        myHolder.binding.setModel(streamModel);
        myHolder.binding.btnJoin.setOnClickListener(view -> {
            StreamModel streamModel2 = list.get(myHolder.getAdapterPosition());
            fragment.setItemData(streamModel2,myHolder.getAdapterPosition());

        });





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public StreamRowBinding binding;

        public MyHolder(@NonNull StreamRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
