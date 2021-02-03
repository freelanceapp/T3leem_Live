package com.t3leem_live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.databinding.JoinCenterRowBinding;
import com.t3leem_live.databinding.JoinGroupRowBinding;
import com.t3leem_live.models.JoinCenterModel;

import java.util.List;

public class JoinGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<JoinCenterModel> list;
    private Context context;
    private LayoutInflater inflater;

    public JoinGroupAdapter(List<JoinCenterModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        JoinGroupRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.join_group_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        JoinCenterModel model = list.get(position);
        myHolder.binding.setModel(model);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public JoinGroupRowBinding binding;

        public MyHolder(@NonNull JoinGroupRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
