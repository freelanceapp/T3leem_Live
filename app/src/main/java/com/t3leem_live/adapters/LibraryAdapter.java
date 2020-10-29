package com.t3leem_live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_classes.ClassesActivity;
import com.t3leem_live.activities_fragments.activity_home_teacher.fragments.Fragment_Library_Teacher;
import com.t3leem_live.activities_fragments.activity_student_home.StudentHomeActivity;
import com.t3leem_live.activities_fragments.activity_student_home.fragments.Fragment_Library_Student;
import com.t3leem_live.databinding.ClassRowBinding;
import com.t3leem_live.databinding.LibraryRowBinding;
import com.t3leem_live.models.StageClassModel;

import java.util.List;

import io.paperdb.Paper;

public class LibraryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StageClassModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private Fragment fragment;
    private int [] background = {R.color.color11,R.color.color12,R.color.color13,R.color.color14,R.color.color15,R.color.color16};

    public LibraryAdapter(List<StageClassModel> list, Context context,Fragment fragment) {
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


        LibraryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.library_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        StageClassModel classModel = list.get(position);
        myHolder.binding.setModel(classModel);
        int i = position%background.length;
        myHolder.binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context,background[i]));

        myHolder.itemView.setOnClickListener(view -> {
            StageClassModel classModel2 = list.get(holder.getAdapterPosition());
            if (fragment instanceof  Fragment_Library_Student){
                Fragment_Library_Student fragment_library_student = (Fragment_Library_Student) fragment;
                fragment_library_student.setItemData(classModel2);

            }else if (fragment instanceof Fragment_Library_Teacher){
                Fragment_Library_Teacher fragment_library_teacher = (Fragment_Library_Teacher) fragment;
                fragment_library_teacher.setItemData(classModel2);
            }
        });





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public LibraryRowBinding binding;

        public MyHolder(@NonNull LibraryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
