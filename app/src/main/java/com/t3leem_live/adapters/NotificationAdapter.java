package com.t3leem_live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.databinding.NotificationRowBinding;
import com.t3leem_live.databinding.SonRowBinding;
import com.t3leem_live.models.NotificationModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.uis.module_parent.activity_home_parent.fragments.Fragment_Home_Parent;
import com.t3leem_live.uis.module_student.activity_notification.NotificationActivity;

import java.util.List;

import io.paperdb.Paper;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NotificationModel> list;
    private Context context;
    private LayoutInflater inflater;
    private NotificationActivity activity;


    public NotificationAdapter(List<NotificationModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (NotificationActivity) context;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        NotificationRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.notification_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder){
            MyHolder myHolder = (MyHolder) holder;
            NotificationModel model= list.get(position);
            myHolder.binding.setModel(model);

            myHolder.binding.btnAccept.setOnClickListener(view -> {
                NotificationModel model2= list.get(holder.getAdapterPosition());
                activity.acceptRefuseGroup(model2,myHolder.getAdapterPosition(),"accepted");

            });


            myHolder.binding.btnRefuse.setOnClickListener(view -> {
                NotificationModel model2= list.get(holder.getAdapterPosition());
                activity.acceptRefuseGroup(model2,myHolder.getAdapterPosition(),"no_accepted");

            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private NotificationRowBinding binding;

        public MyHolder(NotificationRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
