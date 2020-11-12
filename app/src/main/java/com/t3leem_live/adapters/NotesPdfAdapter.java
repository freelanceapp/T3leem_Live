package com.t3leem_live.adapters;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_notes_pdf.NotesPdfActivity;
import com.t3leem_live.activities_fragments.activity_videos.VideosActivity;
import com.t3leem_live.databinding.NotesPdfRowBinding;
import com.t3leem_live.databinding.VideoRowBinding;
import com.t3leem_live.models.VideoLessonsModel;
import com.t3leem_live.tags.Tags;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class NotesPdfAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<VideoLessonsModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private NotesPdfActivity activity;

    public NotesPdfAdapter(List<VideoLessonsModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang","ar");
        activity = (NotesPdfActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        NotesPdfRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.notes_pdf_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        VideoLessonsModel videoLessonsModel = list.get(position);
        myHolder.binding.setLang(lang);
        myHolder.binding.setModel(videoLessonsModel);






    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public NotesPdfRowBinding binding;

        public MyHolder(@NonNull NotesPdfRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }



}