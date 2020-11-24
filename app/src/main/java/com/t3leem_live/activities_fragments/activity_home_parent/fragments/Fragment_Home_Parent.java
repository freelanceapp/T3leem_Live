package com.t3leem_live.activities_fragments.activity_home_parent.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_chat_rooms.ChatRoomsActivity;
import com.t3leem_live.activities_fragments.activity_home_parent.ParentHomeActivity;
import com.t3leem_live.activities_fragments.activity_student_home.StudentHomeActivity;
import com.t3leem_live.activities_fragments.activity_subject_tutorial.SubjectTutorialActivity;
import com.t3leem_live.adapters.MySonsAdapter;
import com.t3leem_live.adapters.StageAdapter;
import com.t3leem_live.databinding.FragmentHomeParentBinding;
import com.t3leem_live.databinding.FragmentHomeStudentBinding;
import com.t3leem_live.models.MySonsDataModel;
import com.t3leem_live.models.RoomDataModel;
import com.t3leem_live.models.StageClassModel;
import com.t3leem_live.models.StageDataModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home_Parent extends Fragment {
    private FragmentHomeParentBinding binding;
    private ParentHomeActivity activity;
    private SkeletonScreen skeletonScreen;
    private UserModel userModel;
    private MySonsAdapter adapter;
    private List<UserModel.User> sonsList;
    private Preferences preferences;
    private String lang;

    public static Fragment_Home_Parent newInstance(){
        return new Fragment_Home_Parent();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_parent,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        sonsList = new ArrayList<>();
        activity = (ParentHomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");

        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new MySonsAdapter(sonsList,activity,this);
        binding.recView.setAdapter(adapter);
        skeletonScreen = Skeleton.bind(binding.recView)
                .adapter(adapter)
                .frozen(true)
                .duration(1000)
                .count(6)
                .shimmer(true)
                .show();

        getMySons();

    }

    private void getMySons() {
        Api.getService(Tags.base_url).getMySons("Bearer "+userModel.getData().getToken(),userModel.getData().getId())
                .enqueue(new Callback<MySonsDataModel>() {
                    @Override
                    public void onResponse(Call<MySonsDataModel> call, Response<MySonsDataModel> response) {
                        skeletonScreen.hide();
                        if (response.isSuccessful()) {
                            if (response.body() != null&&response.body().getData()!=null) {
                                sonsList.clear();

                                if (response.body().getData().size() > 0) {
                                    binding.tvNoSons.setVisibility(View.GONE);
                                    sonsList.addAll(response.body().getData());
                                } else {
                                    binding.tvNoSons.setVisibility(View.VISIBLE);

                                }

                                adapter.notifyDataSetChanged();



                            }
                        } else {
                            skeletonScreen.hide();
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<MySonsDataModel> call, Throwable t) {
                        skeletonScreen.hide();
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }


}
