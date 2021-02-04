package com.t3leem_live.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.t3leem_live.R;
import com.t3leem_live.databinding.NotificationRowBinding;
import com.t3leem_live.databinding.SonRowBinding;
import com.t3leem_live.models.NotificationModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.tags.Tags;
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

        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            NotificationModel model = list.get(position);
            myHolder.binding.setModel(model);


            if (model.getNotification_type().equals("admin_to_users")) {
                myHolder.binding.tvTitle.setText(context.getString(R.string.admin));
                myHolder.binding.tvContent.setVisibility(View.VISIBLE);
                myHolder.binding.tvContent.setText(model.getTitle());
                myHolder.binding.llAction.setVisibility(View.GONE);
                myHolder.binding.image.setImageResource(R.drawable.admin);


            } else if (model.getNotification_type().equals("relationship")) {
                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + model.getFrom_user_fk().getLogo())).resize(512, 512).onlyScaleDown().into(myHolder.binding.image);
                if (model.getMessage().equals("new_request")) {

                    myHolder.binding.tvTitle.setText(model.getFrom_user_fk().getName());
                    myHolder.binding.tvContent.setVisibility(View.VISIBLE);
                    myHolder.binding.tvContent.setText(R.string.new_req);

                    if (model.getAction_type().equals("without_action")) {
                        myHolder.binding.llAction.setVisibility(View.GONE);
                    } else {
                        myHolder.binding.llAction.setVisibility(View.VISIBLE);

                    }
                } else if (model.getMessage().equals("your_request_accepted")) {
                    Picasso.get().load(Uri.parse(Tags.IMAGE_URL + model.getFrom_user_fk().getLogo())).resize(512, 512).onlyScaleDown().into(myHolder.binding.image);

                    myHolder.binding.tvTitle.setText(model.getFrom_user_fk().getName());
                    myHolder.binding.tvContent.setVisibility(View.VISIBLE);
                    myHolder.binding.tvContent.setText(context.getString(R.string.req_accepted));
                    myHolder.binding.llAction.setVisibility(View.GONE);


                } else if (model.getMessage().equals("your_request_refused")) {
                    Picasso.get().load(Uri.parse(Tags.IMAGE_URL + model.getFrom_user_fk().getLogo())).resize(512, 512).onlyScaleDown().into(myHolder.binding.image);

                    myHolder.binding.tvTitle.setText(model.getFrom_user_fk().getName());
                    myHolder.binding.tvContent.setVisibility(View.VISIBLE);
                    myHolder.binding.tvContent.setText(context.getString(R.string.req_accepted));
                    myHolder.binding.llAction.setVisibility(View.GONE);


                }
            } else if (model.getNotification_type().equals("share_teacher")) {
                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + model.getFrom_user_fk().getLogo())).resize(512, 512).onlyScaleDown().into(myHolder.binding.image);

                myHolder.binding.tvTitle.setText(model.getFrom_user_fk().getName());
                myHolder.binding.tvContent.setVisibility(View.VISIBLE);
                myHolder.binding.tvContent.setText(model.getTitle());
                myHolder.binding.llAction.setVisibility(View.GONE);

            } else if (model.getNotification_type().equals("share_center")) {
                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + model.getFrom_user_fk().getLogo())).resize(512, 512).onlyScaleDown().into(myHolder.binding.image);

                myHolder.binding.tvTitle.setText(model.getFrom_user_fk().getName());
                myHolder.binding.tvContent.setVisibility(View.VISIBLE);
                myHolder.binding.tvContent.setText(model.getTitle());
                myHolder.binding.llAction.setVisibility(View.GONE);
            } else if (model.getNotification_type().equals("exam")) {
                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + model.getFrom_user_fk().getLogo())).resize(512, 512).onlyScaleDown().into(myHolder.binding.image);

                myHolder.binding.tvTitle.setText(model.getFrom_user_fk().getName());
                myHolder.binding.tvContent.setVisibility(View.VISIBLE);
                myHolder.binding.tvContent.setText(model.getTitle());
                myHolder.binding.llAction.setVisibility(View.GONE);
            } else if (model.getNotification_type().equals("live_stream")) {
                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + model.getFrom_user_fk().getLogo())).resize(512, 512).onlyScaleDown().into(myHolder.binding.image);

                myHolder.binding.tvTitle.setText(model.getFrom_user_fk().getName());
                myHolder.binding.tvContent.setVisibility(View.VISIBLE);
                myHolder.binding.tvContent.setText(model.getTitle());
                myHolder.binding.llAction.setVisibility(View.GONE);
            }


            myHolder.binding.btnAccept.setOnClickListener(view -> {
                NotificationModel model2 = list.get(holder.getAdapterPosition());
                activity.acceptRefuseGroup(model2, myHolder.getAdapterPosition(), "accepted");

            });


            myHolder.binding.btnRefuse.setOnClickListener(view -> {
                NotificationModel model2 = list.get(holder.getAdapterPosition());
                activity.acceptRefuseGroup(model2, myHolder.getAdapterPosition(), "no_accepted");

            });


            myHolder.itemView.setOnClickListener(v -> {
                NotificationModel model2 = list.get(holder.getAdapterPosition());
                if (model.getNotification_type().equals("exam")) {
                    activity.setItemExam(model2);
                } else if (model.getNotification_type().equals("share_teacher")) {
                    activity.setItemShare(model2, "teacher");
                } else if (model.getNotification_type().equals("share_center")) {
                    activity.setItemShare(model2, "center");
                }
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
