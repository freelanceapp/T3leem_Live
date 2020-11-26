package com.t3leem_live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.uis.module_general.activity_library_details.LibraryDetailsActivity;
import com.t3leem_live.databinding.BooksRowBinding;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.tags.Tags;

import java.util.List;

import io.paperdb.Paper;

public class LibraryBooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StageClassModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private LibraryDetailsActivity activity;

    public LibraryBooksAdapter(List<StageClassModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang","ar");
        activity = (LibraryDetailsActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        BooksRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.books_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        StageClassModel stageClassModel = list.get(position);
        myHolder.binding.setLang(lang);
        myHolder.binding.setModel(stageClassModel);

        myHolder.binding.tvView.setOnClickListener(view -> {
            String url = Tags.IMAGE_URL+list.get(myHolder.getAdapterPosition()).getFile_doc();
            activity.navigateToActivityView(url);
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public BooksRowBinding binding;

        public MyHolder(@NonNull BooksRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }



}
