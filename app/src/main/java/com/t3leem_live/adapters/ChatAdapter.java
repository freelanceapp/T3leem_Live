package com.t3leem_live.adapters;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.squareup.picasso.Picasso;
import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_chat.ChatActivity;
import com.t3leem_live.databinding.ChatImageLeftRowBinding;
import com.t3leem_live.databinding.ChatImageRightRowBinding;
import com.t3leem_live.databinding.ChatMessageAudioLeftRowBinding;
import com.t3leem_live.databinding.ChatMessageAudioRightRowBinding;
import com.t3leem_live.databinding.ChatMessageLeftRowBinding;
import com.t3leem_live.databinding.ChatMessageRightRowBinding;
import com.t3leem_live.models.MessageModel;
import com.t3leem_live.tags.Tags;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int msg_left = 1;
    private final int msg_right = 2;
    private final int img_left = 3;
    private final int img_right = 4;
    private final int sound_left = 5;
    private final int sound_right = 6;
    private final int load = 9;
    private LayoutInflater inflater;
    private List<MessageModel> list;
    private Context context;
    private int current_user_id;
    private ChatActivity activity;
    private SparseBooleanArray array;
    private SparseArray<MediaPlayer> mediaPlayerList;
    private int selected_pos = -1;
    private Handler handler;
    private Runnable runnable;
    private String chat_type;





    public ChatAdapter(List<MessageModel> list, Context context, int current_user_id, String chat_type) {
        this.list = list;
        this.context = context;
        this.current_user_id = current_user_id;
        inflater = LayoutInflater.from(context);
        activity = (ChatActivity) context;
        array = new SparseBooleanArray();
        mediaPlayerList = new SparseArray<>();
        handler = new Handler();
        this.chat_type = chat_type;



    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == msg_left) {
            ChatMessageLeftRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_message_left_row, parent, false);
            return new HolderMsgLeft(binding);
        } else if (viewType == msg_right) {
            ChatMessageRightRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_message_right_row, parent, false);
            return new HolderMsgRight(binding);
        } else if (viewType == img_left) {
            ChatImageLeftRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_image_left_row, parent, false);
            return new HolderImageLeft(binding);
        } else if (viewType == img_right) {
            ChatImageRightRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_image_right_row, parent, false);
            return new HolderImageRight(binding);
        }else if (viewType == sound_left) {
            ChatMessageAudioLeftRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_message_audio_left_row, parent, false);
            return new SoundLeftHolder(binding);

        } else {
            ChatMessageAudioRightRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_message_audio_right_row, parent, false);
            return new SoundRightHolder(binding);
        }




    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel model = list.get(position);

        if (!model.isLoaded()){

            if (holder instanceof SoundLeftHolder){

                SoundLeftHolder soundLeftHolder = (SoundLeftHolder) holder;
                MessageModel model2 = list.get(soundLeftHolder.getAdapterPosition());

                createMediaPlayer(model2,soundLeftHolder.getAdapterPosition());



            }else if (holder instanceof SoundRightHolder){
                SoundRightHolder soundRightHolder = (SoundRightHolder) holder;

               MessageModel model2 = list.get(soundRightHolder.getAdapterPosition());

                createMediaPlayer(model2,soundRightHolder.getAdapterPosition());

            }


        }
        else {

            if (holder instanceof SoundLeftHolder){

                SoundLeftHolder soundLeftHolder = (SoundLeftHolder) holder;
                soundLeftHolder.binding.imagePlay.setVisibility(View.VISIBLE);
                soundLeftHolder.binding.progBar.setVisibility(View.GONE);

                MediaPlayer mediaPlayer = mediaPlayerList.get(soundLeftHolder.getAdapterPosition());

                if (selected_pos==soundLeftHolder.getAdapterPosition()){

                    if (mediaPlayer.isPlaying()){

                        soundLeftHolder.binding.imagePlay.setImageResource(R.drawable.ic_pause);

                    }else {

                        soundLeftHolder.binding.imagePlay.setImageResource(R.drawable.ic_play);

                    }
                }else {
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        soundLeftHolder.binding.imagePlay.setImageResource(R.drawable.ic_play);

                    }
                }

            }else if (holder instanceof SoundRightHolder){
                SoundRightHolder soundRightHolder = (SoundRightHolder) holder;
                soundRightHolder.binding.imagePlay.setVisibility(View.VISIBLE);
                soundRightHolder.binding.progBar.setVisibility(View.GONE);
                MediaPlayer mediaPlayer = mediaPlayerList.get(soundRightHolder.getAdapterPosition());

                if (selected_pos==soundRightHolder.getAdapterPosition()){

                    if (mediaPlayer.isPlaying()){

                        soundRightHolder.binding.imagePlay.setImageResource(R.drawable.ic_pause);

                    }else {

                        soundRightHolder.binding.imagePlay.setImageResource(R.drawable.ic_play);

                    }
                }else {
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        soundRightHolder.binding.imagePlay.setImageResource(R.drawable.ic_play);

                    }
                }




            }
        }




        ///////////////////



        if (holder instanceof HolderMsgLeft) {
            HolderMsgLeft holderMsgLeft = (HolderMsgLeft) holder;
            holderMsgLeft.binding.setModel(model);
            holderMsgLeft.binding.tvTime.setText(getTime(Long.parseLong(model.getDate())));

        } else if (holder instanceof HolderMsgRight) {
            HolderMsgRight holderMsgRight = (HolderMsgRight) holder;
            holderMsgRight.binding.setModel(model);
            holderMsgRight.binding.tvTime.setText(getTime(Long.parseLong(model.getDate())));


        } else if (holder instanceof HolderImageLeft) {
            HolderImageLeft holderImageLeft = (HolderImageLeft) holder;
            holderImageLeft.binding.setModel(model);
            holderImageLeft.binding.tvTime.setText(getTime(Long.parseLong(model.getDate())));
            Picasso.get().load(Uri.parse(Tags.IMAGE_URL+model.getImage())).into(holderImageLeft.binding.imageChat);

        } else if (holder instanceof HolderImageRight) {
            HolderImageRight holderImageRight = (HolderImageRight) holder;
            holderImageRight.binding.tvTime.setText(getTime(Long.parseLong(model.getDate())));
            Picasso.get().load(Uri.parse(Tags.IMAGE_URL+model.getImage())).into(holderImageRight.binding.image);




        }  else if (holder instanceof SoundRightHolder) {

            SoundRightHolder soundRightHolder = (SoundRightHolder) holder;
            MessageModel messageModel = list.get(holder.getAdapterPosition());
            soundRightHolder.BindData(messageModel);

            soundRightHolder.binding.imagePlay.setOnClickListener(v -> {
                MediaPlayer mediaPlayer = mediaPlayerList.get(soundRightHolder.getAdapterPosition());

                if (mediaPlayer.isPlaying()){
                    pausePlay(mediaPlayer,soundRightHolder.getAdapterPosition());
                }else {
                    playSound(soundRightHolder.getAdapterPosition());

                }
            });



        } else if (holder instanceof SoundLeftHolder) {
            SoundLeftHolder soundLeftHolder = (SoundLeftHolder) holder;
            MessageModel messageModel = list.get(holder.getAdapterPosition());
            soundLeftHolder.BindData(messageModel);
            soundLeftHolder.binding.setModel(model);
            soundLeftHolder.binding.imagePlay.setOnClickListener(v -> {
                MediaPlayer mediaPlayer = mediaPlayerList.get(soundLeftHolder.getAdapterPosition());

                if (mediaPlayer.isPlaying()){
                    pausePlay(mediaPlayer,soundLeftHolder.getAdapterPosition());
                }else {
                    playSound(soundLeftHolder.getAdapterPosition());

                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private String getTime(long time) {
        return new SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(new Date(time * 1000));
    }


    public class HolderMsgLeft extends RecyclerView.ViewHolder {
        private ChatMessageLeftRowBinding binding;

        public HolderMsgLeft(@NonNull ChatMessageLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class HolderMsgRight extends RecyclerView.ViewHolder {
        private ChatMessageRightRowBinding binding;

        public HolderMsgRight(@NonNull ChatMessageRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class HolderImageLeft extends RecyclerView.ViewHolder {
        private ChatImageLeftRowBinding binding;

        public HolderImageLeft(@NonNull ChatImageLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class HolderImageRight extends RecyclerView.ViewHolder {
        private ChatImageRightRowBinding binding;

        public HolderImageRight(@NonNull ChatImageRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }






    public class SoundRightHolder extends RecyclerView.ViewHolder {

        private ChatMessageAudioRightRowBinding binding;


        public SoundRightHolder(ChatMessageAudioRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


        public void BindData(final MessageModel messageModel) {

            binding.progBar2.setMax(messageModel.getMax_duration());
            binding.progBar2.setProgress(messageModel.getProgress());


        }






    }

    public class SoundLeftHolder extends RecyclerView.ViewHolder {
        private ChatMessageAudioLeftRowBinding binding;


        public SoundLeftHolder(ChatMessageAudioLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void BindData(final MessageModel messageModel) {

            binding.progBar2.setMax(messageModel.getMax_duration());
            binding.progBar2.setProgress(messageModel.getProgress());



        }









    }


    private void createMediaPlayer(MessageModel messageModel,int pos) {
        new AudioTask(messageModel,pos).execute(messageModel);


    }

    private void playSound(int pos){
        this.selected_pos = pos;
        MediaPlayer mediaPlayer = mediaPlayerList.get(pos);
        mediaPlayer.start();
        notifyDataSetChanged();
        new ProgressTask(mediaPlayer,pos).execute(list.get(pos));
        mediaPlayer.setOnCompletionListener(mp -> {
            notifyItemChanged(pos);
        });



    }

    private void pausePlay(MediaPlayer mediaPlayer, int pos){
        mediaPlayer.pause();
        notifyItemChanged(pos);

    }

    public void stopPlay() {

        MediaPlayer mediaPlayer =  mediaPlayerList.get(selected_pos);

        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public int getItemViewType(int position) {

        MessageModel messageModel = list.get(position);

        if (messageModel==null){
            return load;
        }else {

            if (messageModel.getType().equals("message")){

                if (messageModel.getFrom_user_fk().getId()==current_user_id){

                    return msg_right;
                }else {
                    return msg_left;
                }
            }else if (messageModel.getType().equals("image")){

                if (messageModel.getFrom_user_fk().getId()==current_user_id){

                    return img_right;
                }else {
                    return img_left;
                }
            }else {

                if (messageModel.getFrom_user_fk().getId()==current_user_id){

                    return sound_right;
                }else {
                    return sound_left;
                }
            }


        }


    }




    private class AudioTask extends AsyncTask<MessageModel, Void, MediaPlayer> {
        private MessageModel messageModel;
        int pos;

        public AudioTask(MessageModel messageModel, int pos) {
            this.messageModel = messageModel;
            this.pos = pos;
        }

        @Override
        protected MediaPlayer doInBackground(MessageModel... messageModels) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {

                mediaPlayer.setDataSource(Tags.IMAGE_URL + messageModels[0].getVoice());
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setVolume(100.0f, 100.0f);
                mediaPlayer.setLooping(false);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(mp -> onPostExecute(mediaPlayer));





            } catch (IOException e) {
                Log.e("eeeex", e.getMessage());


            }

            return null;
        }

        @Override
        protected void onPostExecute(MediaPlayer mediaPlayer) {
            super.onPostExecute(mediaPlayer);
            if (mediaPlayer!=null){
                mediaPlayerList.put(pos,mediaPlayer);
                messageModel.setLoaded(true);
                messageModel.setMax_duration(mediaPlayer.getDuration());
                list.set(pos,messageModel);
                notifyItemChanged(pos);
            }


        }
    }

    private class ProgressTask extends AsyncTask<MessageModel, Integer, Void> {
        private MediaPlayer mediaPlayer;
        private int pos;

        public ProgressTask(MediaPlayer mediaPlayer, int pos) {
            this.mediaPlayer = mediaPlayer;
            this.pos = pos;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (handler!=null&&runnable!=null){
                handler.removeCallbacks(runnable);
            }

        }

        @Override
        protected Void doInBackground(MessageModel... messageModels) {

            runnable = () -> {
                int progress = mediaPlayer.getCurrentPosition();
                if (mediaPlayer.isPlaying()){
                    publishProgress(progress);
                    doInBackground(messageModels[0]);

                }else {
                    publishProgress(0);

                    handler.removeCallbacks(runnable);
                }

            };
            handler.postDelayed(runnable,100);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            MessageModel model = list.get(pos);
            model.setProgress(values[0]);
            list.set(pos,model);
            notifyItemChanged(pos);
        }
    }


}
