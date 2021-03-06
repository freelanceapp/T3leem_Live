package com.t3leem_live.uis.module_student.activity_videos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.t3leem_live.R;
import com.t3leem_live.adapters.VideoAdapter;
import com.t3leem_live.databinding.ActivityVideosBinding;
import com.t3leem_live.databinding.DialogPriceAlertBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.models.VideoLessonsDataModel;
import com.t3leem_live.models.VideoLessonsModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideosActivity extends AppCompatActivity {

    private ActivityVideosBinding binding;
    private UserModel userModel;
    private Preferences preferences;
    private StageClassModel stageClassModel;
    private List<VideoLessonsModel> videoLessonsModelList;
    private VideoAdapter adapter;
    private String lang;
    private SimpleExoPlayer player;
    private int currentWindow = 0;
    private long currentPosition = 0;
    private boolean playWhenReady = true;
    private int selectedPos = 0;
    private Uri uri = null;
    private VideoLessonsModel selectedVideoLessonsModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_videos);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        stageClassModel = (StageClassModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        videoLessonsModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBarBuffering.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.gray5), PorterDuff.Mode.SRC_IN);

        adapter = new VideoAdapter(videoLessonsModelList, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);
        getVideos();

    }


    private void getVideos() {

        Api.getService(Tags.base_url)
                .getVideos(userModel.getData().getId(), stageClassModel.getStage_id(), stageClassModel.getClass_id(), stageClassModel.getDepartment_id(), String.valueOf(stageClassModel.getId()), "video")
                .enqueue(new Callback<VideoLessonsDataModel>() {
                    @Override
                    public void onResponse(Call<VideoLessonsDataModel> call, Response<VideoLessonsDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null) {
                                videoLessonsModelList.clear();
                                videoLessonsModelList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                if (videoLessonsModelList.size() > 0) {
                                    binding.recView.setVisibility(View.VISIBLE);
                                    binding.expandableLayout.expand(true);
                                    if (videoLessonsModelList.get(0).getVideo_or_pdf__payment_fk() != null) {
                                        selectedPos = 0;
                                        selectedVideoLessonsModel = videoLessonsModelList.get(selectedPos);
                                        adapter.setSelectedPos(selectedPos);
                                        binding.setModel(selectedVideoLessonsModel);
                                        uri = Uri.parse(Tags.IMAGE_URL + selectedVideoLessonsModel.getFile_doc());
                                        initPlayer(uri, selectedVideoLessonsModel, selectedPos);
                                    }

                                    binding.llNoVideos.setVisibility(View.GONE);
                                } else {
                                    binding.recView.setVisibility(View.GONE);
                                    binding.llNoVideos.setVisibility(View.VISIBLE);

                                }
                            }

                        } else {
                            binding.progBar.setVisibility(View.GONE);

                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(VideosActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(VideosActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoLessonsDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(VideosActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(VideosActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    public void playVideo(VideoLessonsModel videoLessonsModel, int selectedPos) {
        selectedVideoLessonsModel = videoLessonsModel;
        this.selectedPos = selectedPos;
        if (!binding.expandableLayout.isExpanded()) {
            binding.expandableLayout.expand(true);
        }
        if (videoLessonsModel.getVideo_or_pdf__payment_fk() != null) {
            Log.e("dd", "dd");
            binding.setModel(videoLessonsModel);

            uri = Uri.parse(Tags.IMAGE_URL + videoLessonsModel.getFile_doc());
            initPlayer(uri, selectedVideoLessonsModel, selectedPos);
        } else {
            createDialogAlert(videoLessonsModel.getPrice());
        }

    }


    private void createDialogAlert(double price) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        DialogPriceAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_price_alert, null, false);

        String pr = getString(R.string.watch_video_for) + " " + price + " " + getString(R.string.egp);
        binding.tvMsg.setText(pr);
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss()

        );
        binding.btnAccept.setOnClickListener(v ->
                {
                    if (userModel.getData().getStudent_money() >= price) {
                        discount();
                    } else {
                        Toast.makeText(this, R.string.balance_not_ava, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    private void discount() {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .payment(userModel.getData().getId(), "video_or_pdf", selectedVideoLessonsModel.getId(), selectedVideoLessonsModel.getPrice())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            selectedVideoLessonsModel.setVideo_or_pdf__payment_fk(new VideoLessonsModel.StudentPayment());
                            videoLessonsModelList.set(selectedPos, selectedVideoLessonsModel);
                            adapter.notifyItemChanged(selectedPos);
                            uri = Uri.parse(Tags.IMAGE_URL + selectedVideoLessonsModel.getFile_doc());
                            adapter.setSelectedPos(selectedPos);
                            initPlayer(uri, selectedVideoLessonsModel, selectedPos);
                            double balance = userModel.getData().getStudent_money() - selectedVideoLessonsModel.getPrice();
                            userModel.getData().setStudent_money(balance);
                            preferences.create_update_userdata(VideosActivity.this, userModel);
                            //playVideo(selectedVideoLessonsModel,selectedPos);

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(VideosActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 422) {
                                Toast.makeText(VideosActivity.this, R.string.already_paid, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(VideosActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(VideosActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(VideosActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    public void initPlayer(Uri uri, VideoLessonsModel selectedVideoLessonsModel, int selectedPos) {
        this.selectedPos = selectedPos;
        this.selectedVideoLessonsModel = selectedVideoLessonsModel;

        Log.e("pos", this.selectedPos + "____" + this.selectedVideoLessonsModel.getId());

        if (this.selectedVideoLessonsModel.getVideo_or_pdf__payment_fk() != null) {


            if (player == null) {
                DefaultTrackSelector trackSelector = new DefaultTrackSelector();
                trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd());
                player = ExoPlayerFactory.newSimpleInstance(this);
                binding.player.setPlayer(player);
                MediaSource mediaSource = buildMediaSource(uri);

                player.seekTo(currentWindow, currentPosition);
                player.setPlayWhenReady(playWhenReady);
                player.prepare(mediaSource);
            } else {
                currentWindow = 0;
                currentPosition = 0;
                MediaSource mediaSource = buildMediaSource(uri);

                player.seekTo(currentWindow, currentPosition);
                player.setPlayWhenReady(playWhenReady);
                player.prepare(mediaSource);
            }

            player.addListener(new Player.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playWhenReady) {
                        if (playbackState == Player.STATE_BUFFERING) {
                            binding.progBarBuffering.setVisibility(View.VISIBLE);
                        } else if (playbackState == Player.STATE_READY) {
                            binding.progBarBuffering.setVisibility(View.GONE);

                        } else if (playbackState == Player.STATE_ENDED) {
                            binding.progBarBuffering.setVisibility(View.GONE);
                            currentWindow = 0;
                            currentPosition = 0;
                            initPlayer(uri, selectedVideoLessonsModel, selectedPos);
                        } else if (playbackState == Player.TIMELINE_CHANGE_REASON_RESET) {
                            binding.progBarBuffering.setVisibility(View.VISIBLE);

                        } else {
                            binding.progBarBuffering.setVisibility(View.GONE);

                        }
                    }
                }


            });

        } else {
            createDialogAlert(this.selectedVideoLessonsModel.getPrice());

        }


    }

    private MediaSource buildMediaSource(Uri uri) {
        String userAgent = "exoPlayer_Ta3leem";


        if (uri.getLastPathSegment().contains("mp3") || uri.getLastPathSegment().contains("mp4")) {
            return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri);
        } else if (uri.getLastPathSegment().contains("m3u8")) {

            return new HlsMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri);
        } else {

            DefaultDashChunkSource.Factory factory = new DefaultDashChunkSource.Factory(new DefaultHttpDataSourceFactory(userAgent));

            return new DashMediaSource.Factory(factory, new DefaultDataSourceFactory(this, userAgent)).createMediaSource(uri);
        }

    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            currentWindow = player.getCurrentWindowIndex();
            currentPosition = player.getCurrentPosition();
            player.release();
            player = null;

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            if (uri != null) {
                initPlayer(uri, selectedVideoLessonsModel, selectedPos);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.SDK_INT < 24 || player == null) {
            if (uri != null) {
                initPlayer(uri, selectedVideoLessonsModel, selectedPos);

            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }


}