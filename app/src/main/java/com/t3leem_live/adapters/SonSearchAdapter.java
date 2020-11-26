package com.t3leem_live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.databinding.SonRowBinding;
import com.t3leem_live.databinding.SonSearchRowBinding;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.uis.module_parent.activity_home_parent.fragments.Fragment_Home_Parent;
import com.t3leem_live.uis.module_parent.activity_parent_add_son.ParentAddSonActivity;

import java.util.List;

import io.paperdb.Paper;

public class SonSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserModel.User> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private ParentAddSonActivity activity;

    public SonSearchAdapter(List<UserModel.User> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang","ar");
        activity = (ParentAddSonActivity) context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SonSearchRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.son_search_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder){
            MyHolder myHolder = (MyHolder) holder;
            UserModel.User model= list.get(position);
            myHolder.binding.setModel(model);
            myHolder.binding.tvStageClassDepartment.setText(getStageData(model));

            myHolder.binding.flAddSon.setOnClickListener(view -> {
                UserModel.User model2= list.get(myHolder.getAdapterPosition());
                activity.addSon(model2);
            });

        }

    }

    private String getStageData(UserModel.User user){
        String data="";
        if (user.getStage_fk()!=null){
            if (lang.equals("ar")){
                data += user.getStage_fk().getTitle();

            }else {
                data += user.getStage_fk().getTitle_en();

            }
        }
        if (user.getClass_fk()!=null){
            data +=",";
            if (lang.equals("ar")){
                data += user.getClass_fk().getTitle();

            }else {
                data += user.getClass_fk().getTitle_en();

            }
        }
        if (user.getDepartment_fk()!=null){

            data +=",";
            if (lang.equals("ar")){
                data += user.getDepartment_fk().getTitle();

            }else {
                data += user.getDepartment_fk().getTitle_en();

            }
        }
        return data;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private SonSearchRowBinding binding;

        public MyHolder(SonSearchRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
