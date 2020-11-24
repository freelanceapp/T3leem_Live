package com.t3leem_live.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_home_parent.fragments.Fragment_Home_Parent;
import com.t3leem_live.activities_fragments.activity_teacher_students.TeacherStudentsActivity;
import com.t3leem_live.databinding.LoadMoreRowBinding;
import com.t3leem_live.databinding.SonRowBinding;
import com.t3leem_live.databinding.StudentRowBinding;
import com.t3leem_live.models.TeacherStudentsModel;
import com.t3leem_live.models.UserModel;

import java.util.List;

public class MySonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserModel.User> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment_Home_Parent fragment;

    public MySonsAdapter(List<UserModel.User> list, Context context, Fragment_Home_Parent fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SonRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.son_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder){
            MyHolder myHolder = (MyHolder) holder;
            UserModel.User model= list.get(position);
            myHolder.binding.setModel(model);



        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private SonRowBinding binding;

        public MyHolder(SonRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
