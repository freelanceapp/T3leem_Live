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

import io.paperdb.Paper;

public class MySonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserModel.User> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment_Home_Parent fragment;
    private String lang;

    public MySonsAdapter(List<UserModel.User> list, Context context, Fragment_Home_Parent fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
        Paper.init(context);
        lang = Paper.book().read("lang","ar");
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
            myHolder.binding.tvStageClassDepartment.setText(getStageData(model));



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
        private SonRowBinding binding;

        public MyHolder(SonRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
