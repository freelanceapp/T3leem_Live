package com.t3leem_live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_teacher_group.TeacherGroupActivity;
import com.t3leem_live.activities_fragments.activity_teacher_groups_chat.TeacherGroupChatActivity;
import com.t3leem_live.databinding.TeacherGroupChatRowBinding;
import com.t3leem_live.databinding.TeacherGroupRowBinding;
import com.t3leem_live.models.TeacherGroupModel;

import java.util.List;

import io.paperdb.Paper;

public class TeacherGroupsChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TeacherGroupModel> list;
    private Context context;
    private LayoutInflater inflater;
    private TeacherGroupChatActivity activity;

    public TeacherGroupsChatAdapter(List<TeacherGroupModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        activity = (TeacherGroupChatActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        TeacherGroupChatRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.teacher_group_chat_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        TeacherGroupModel model = list.get(position);
        myHolder.binding.setModel(model);
        myHolder.binding.llCreateChat.setOnClickListener(view -> {
            TeacherGroupModel model2 = list.get(myHolder.getAdapterPosition());
            activity.createGroupChat(model2);
        });






    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public TeacherGroupChatRowBinding binding;

        public MyHolder(@NonNull TeacherGroupChatRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }



}
