package com.t3leem_live.adapters;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_videos.VideosActivity;
import com.t3leem_live.databinding.SummaryRowBinding;
import com.t3leem_live.databinding.VideoRowBinding;
import com.t3leem_live.models.SummaryModel;
import com.t3leem_live.models.VideoLessonsModel;
import com.t3leem_live.tags.Tags;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<VideoLessonsModel> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int selectedPos = 0;
    private VideosActivity activity;

    public VideoAdapter(List<VideoLessonsModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang","ar");
        activity = (VideosActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        VideoRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.video_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        VideoLessonsModel videoLessonsModel = list.get(position);
        myHolder.binding.setLang(lang);
        myHolder.binding.setModel(videoLessonsModel);

        String url = Tags.IMAGE_URL+videoLessonsModel.getFile_doc();
        videoDuration(((MyHolder) holder).binding,url);

        if (position==selectedPos){
            myHolder.binding.view.setVisibility(View.VISIBLE);
            myHolder.binding.imageStatus.setVisibility(View.VISIBLE);
            myHolder.binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.gray9));
        }else {
            myHolder.binding.view.setVisibility(View.GONE);
            myHolder.binding.imageStatus.setVisibility(View.GONE);
            myHolder.binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.gray8));

        }


        myHolder.itemView.setOnClickListener(view -> {
            int pos = holder.getAdapterPosition();
            if (selectedPos!=pos){
                selectedPos = pos;
                VideoLessonsModel v2 = list.get(selectedPos);
                activity.playVideo(v2);
                notifyDataSetChanged();

            }

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public VideoRowBinding binding;

        public MyHolder(@NonNull VideoRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }



    private void videoDuration(VideoRowBinding binding,String url ){
        new Task(binding).execute(Uri.parse(url));
    }

    public static String convertMillieToHMmSs(long millie) {
        long seconds = (millie / 1000);
        long second = seconds % 60;
        long minute = (seconds / 60) % 60;
        long hour = (seconds / (60 * 60)) % 24;

        String result = "";
        if (hour > 0) {
            return String.format(Locale.ENGLISH,"%02d:%02d:%02d", hour, minute, second);
        }
        else {
            return String.format(Locale.ENGLISH,"%02d:%02d" , minute, second);
        }

    }

    public class Task extends AsyncTask<Uri,Void,String>{
        private VideoRowBinding binding;

        public Task(VideoRowBinding binding) {
            this.binding = binding;
        }

        @Override
        protected String doInBackground(Uri... uris) {
            String duration="";
            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(String.valueOf(uris[0]), new HashMap<String, String>());
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInMilliSec = Long.parseLong(time);
                retriever.release();
                duration=convertMillieToHMmSs(timeInMilliSec); //use this duration

            }catch (Exception e){

            }


            return duration;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            binding.setDuration(s);
        }
    }

}
