package com.t3leem_live.uis.module_teacher.activity_teacher_video;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

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
import com.t3leem_live.databinding.ActivityTeacherVideoBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.TeacherModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.tags.Tags;

import io.paperdb.Paper;

public class TeacherVideoActivity extends AppCompatActivity {
    private ActivityTeacherVideoBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private SimpleExoPlayer player;
    private int currentWindow = 0;
    private long currentPosition = 0;
    private boolean playWhenReady = true;
    private Uri uri = null;
    private TeacherModel teacherModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_video);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        teacherModel = (TeacherModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setLang(lang);
        binding.setModel(teacherModel);
        binding.llBack.setOnClickListener(view ->finish());
        uri = Uri.parse(Tags.IMAGE_URL+teacherModel.getTeacher_video());
        initPlayer(uri);
    }

    private void initPlayer(Uri uri) {
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
                    }else if (playbackState==Player.STATE_READY){
                        binding.progBarBuffering.setVisibility(View.GONE);

                    }else if (playbackState == Player.STATE_ENDED) {
                        binding.progBarBuffering.setVisibility(View.GONE);
                        currentWindow = 0;
                        currentPosition = 0;
                        initPlayer(uri);
                    } else if (playbackState == Player.TIMELINE_CHANGE_REASON_RESET){
                        binding.progBarBuffering.setVisibility(View.VISIBLE);

                    }else {
                        binding.progBarBuffering.setVisibility(View.GONE);

                    }
                }
            }


        });


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
                initPlayer(uri);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.SDK_INT < 24 || player == null) {
            if (uri != null) {
                initPlayer(uri);

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