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
import com.t3leem_live.databinding.JoinCenterRowBinding;
import com.t3leem_live.models.CenterGroupModel;
import com.t3leem_live.models.JoinCenterModel;
import com.t3leem_live.uis.module_center_course.activity_home_center.fragments.Fragment_Home_Center;

import java.util.List;

import io.paperdb.Paper;

public class JoinCenterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<JoinCenterModel> list;
    private Context context;
    private LayoutInflater inflater;

    public JoinCenterAdapter(List<JoinCenterModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        JoinCenterRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.join_center_row, parent, false);
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
        public JoinCenterRowBinding binding;

        public MyHolder(@NonNull JoinCenterRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
