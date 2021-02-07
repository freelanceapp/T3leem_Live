package com.t3leem_live.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.t3leem_live.R;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_general.activity_chat_rooms.ChatRoomsActivity;
import com.t3leem_live.databinding.LoadMoreRowBinding;
import com.t3leem_live.databinding.RoomRowBinding;
import com.t3leem_live.models.RoomModel;

import java.util.List;

public class ChatRoomsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DATA = 1;
    private final int LOAD = 2;
    private List<RoomModel> list;
    private Context context;
    private LayoutInflater inflater;
    private ChatRoomsActivity activity;
    private String user_type;

    public ChatRoomsAdapter(List<RoomModel> list, Context context, String user_type) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (ChatRoomsActivity) context;
        this.user_type = user_type;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == DATA) {
            RoomRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.room_row, parent, false);
            return new MyHolder(binding);
        } else {
            LoadMoreRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.load_more_row, parent, false);
            return new LoadMoreHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            RoomModel model = list.get(position);
            myHolder.binding.setModel(model);


            if (model.getRoom_status().equals("group")) {
                myHolder.binding.image.setImageResource(R.drawable.ic_chat_group);
            } else {
                if (model.getUser_type().equals("parent")) {
                    if (user_type.equals("parent")) {
                        Picasso.get().load(Uri.parse(Tags.IMAGE_URL + model.getTo_user_fk().getLogo())).into(myHolder.binding.image);

                    } else {
                        Picasso.get().load(Uri.parse(Tags.IMAGE_URL + model.getFrom_user_fk().getLogo())).into(myHolder.binding.image);

                    }
                } else {
                    myHolder.binding.image.setImageResource(R.drawable.ic_avatar);

                }
            }

            myHolder.itemView.setOnClickListener(view -> {
                RoomModel model2 = list.get(myHolder.getAdapterPosition());
                activity.navigateToChatActivity(model2);
            });


        } else if (holder instanceof LoadMoreHolder) {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.prgBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.color1), PorterDuff.Mode.SRC_IN);
            loadMoreHolder.binding.prgBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private RoomRowBinding binding;

        public MyHolder(RoomRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    public static class LoadMoreHolder extends RecyclerView.ViewHolder {
        private LoadMoreRowBinding binding;

        public LoadMoreHolder(LoadMoreRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == null) {
            return LOAD;
        } else {
            return DATA;
        }
    }
}
