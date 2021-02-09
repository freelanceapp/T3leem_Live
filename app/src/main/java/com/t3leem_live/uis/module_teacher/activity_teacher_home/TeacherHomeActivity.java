package com.t3leem_live.uis.module_teacher.activity_teacher_home;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.iid.FirebaseInstanceId;
import com.t3leem_live.R;
import com.t3leem_live.databinding.ActivityTeacherHomeBinding;
import com.t3leem_live.models.NotFireModel;
import com.t3leem_live.models.NotificationCountModel;
import com.t3leem_live.uis.module_student.activity_notification.NotificationActivity;
import com.t3leem_live.uis.module_student.activity_student_home.StudentHomeActivity;
import com.t3leem_live.uis.module_teacher.activity_teacher_home.fragments.Fragment_Home_Teacher;
import com.t3leem_live.uis.module_teacher.activity_teacher_home.fragments.Fragment_Library_Teacher;
import com.t3leem_live.uis.module_teacher.activity_teacher_home.fragments.Fragment_Profile_Teacher;
import com.t3leem_live.uis.module_general.activity_login.LoginActivity;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherHomeActivity extends AppCompatActivity {
    private ActivityTeacherHomeBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private FragmentManager fragmentManager;
    private Fragment_Home_Teacher fragment_home_teacher;
    private Fragment_Library_Teacher fragment_library_teacher;
    private Fragment_Profile_Teacher fragment_profile_teacher;
    private int current_pos = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_home);
        initView();


    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        displayFragmentHomeTeacher();
        setUpBottomNavigation();
        getNotificationCount();
        updateTokenFireBase();
        binding.flNotification.setOnClickListener(view -> {
            readNotificationCount();
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
        });

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }


    private void getNotificationCount() {
        Api.getService(Tags.base_url).getNotificationCount("Bearer " + userModel.getData().getToken(), userModel.getData().getId())
                .enqueue(new Callback<NotificationCountModel>() {
                    @Override
                    public void onResponse(Call<NotificationCountModel> call, Response<NotificationCountModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                binding.setCount(String.valueOf(response.body().getData()));
                            }
                        } else {
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<NotificationCountModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(TeacherHomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(TeacherHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void readNotificationCount() {
        Api.getService(Tags.base_url).readNotificationCount("Bearer " + userModel.getData().getToken(), userModel.getData().getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                binding.setCount("0");
                            }
                        } else {
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(TeacherHomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(TeacherHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home), R.drawable.ic_nav_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.library), R.drawable.ic_nav_library);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.profile), R.drawable.ic_nav_user);

        binding.ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        binding.ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.gray0));
        binding.ahBottomNav.setTitleTextSizeInSp(13, 13);
        binding.ahBottomNav.setForceTint(true);
        binding.ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.color1));
        binding.ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.black));

        binding.ahBottomNav.addItem(item1);
        binding.ahBottomNav.addItem(item2);
        binding.ahBottomNav.addItem(item3);


        binding.ahBottomNav.setOnTabSelectedListener((position, wasSelected) -> {
            switch (position) {
                case 0:
                    displayFragmentHomeTeacher();
                    break;
                case 1:
                    displayFragmentLibraryTeacher();
                    break;
                case 2:
                    displayFragmentProfileTeacher();
                    break;


            }
            return false;
        });

        updateBottomNavigationPosition(0);

    }

    public void updateBottomNavigationPosition(int pos) {

        binding.ahBottomNav.setCurrentItem(pos, false);

    }

    public void displayFragmentHomeTeacher() {
        if (fragment_home_teacher == null) {
            fragment_home_teacher = Fragment_Home_Teacher.newInstance();
        }

        if (fragment_library_teacher != null && fragment_library_teacher.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_library_teacher).commit();
        }
        if (fragment_profile_teacher != null && fragment_profile_teacher.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_profile_teacher).commit();
        }

        if (fragment_home_teacher.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_home_teacher).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home_teacher, "fragment_home_teacher").addToBackStack("fragment_home_teacher").commit();

        }

        updateBottomNavigationPosition(0);
    }


    public void displayFragmentLibraryTeacher() {
        if (fragment_library_teacher == null) {
            fragment_library_teacher = Fragment_Library_Teacher.newInstance();
        }

        if (fragment_home_teacher != null && fragment_home_teacher.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_home_teacher).commit();
        }
        if (fragment_profile_teacher != null && fragment_profile_teacher.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_profile_teacher).commit();
        }


        if (fragment_library_teacher.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_library_teacher).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_library_teacher, "fragment_library_teacher").addToBackStack("fragment_library_teacher").commit();

        }

        updateBottomNavigationPosition(1);
    }


    public void displayFragmentProfileTeacher() {
        if (fragment_profile_teacher == null) {
            fragment_profile_teacher = Fragment_Profile_Teacher.newInstance();
        }

        if (fragment_home_teacher != null && fragment_home_teacher.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_home_teacher).commit();
        }
        if (fragment_library_teacher != null && fragment_library_teacher.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_library_teacher).commit();
        }

        if (fragment_profile_teacher.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_profile_teacher).commit();
            fragment_profile_teacher.updateUserData();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile_teacher, "fragment_profile_teacher").addToBackStack("fragment_profile_teacher").commit();

        }

        updateBottomNavigationPosition(2);
    }

    private void updateTokenFireBase() {

        FirebaseInstanceId.getInstance()
                .getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult().getToken();

                try {
                    Api.getService(Tags.base_url)
                            .updateFirebaseToken("Bearer "+userModel.getData().getToken(),token, userModel.getData().getId(), "android")
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        userModel.getData().setFireBaseToken(token);
                                        preferences.create_update_userdata(TeacherHomeActivity.this, userModel);
                                        Log.e("token", "updated successfully");
                                    } else {
                                        try {

                                            Log.e("errorToken", response.code() + "_" + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    try {

                                        if (t.getMessage() != null) {
                                            Log.e("errorToken2", t.getMessage());
                                            if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                                Toast.makeText(TeacherHomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(TeacherHomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    } catch (Exception e) {
                                    }
                                }
                            });
                } catch (Exception e) {


                }

            }
        });
    }

    public void deleteFirebaseToken() {
        if (userModel == null) {
            return;
        } else {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.show();

            Api.getService(Tags.base_url)
                    .deleteFirebaseToken("Bearer "+userModel.getData().getToken(),userModel.getData().getFireBaseToken(), userModel.getData().getId())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {

                                logout(dialog);


                            } else {
                                dialog.dismiss();
                                try {
                                    Log.e("error", response.code() + "__" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(TeacherHomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TeacherHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(TeacherHomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(TeacherHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("Error", e.getMessage() + "__");
                            }
                        }
                    });
        }

    }

    public void logout(ProgressDialog dialog) {
        Api.getService(Tags.base_url)
                .logout("Bearer " + userModel.getData().getToken())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            preferences.clear(TeacherHomeActivity.this);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            if (notificationManager != null) {
                                notificationManager.cancel(Tags.not_tag, Tags.not_id);
                            }
                            navigateToLoginActivity();
                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(TeacherHomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TeacherHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(TeacherHomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TeacherHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenForNotification(NotFireModel notFireModel){
        if (userModel.getData().getUser_type().equals("parent")){
            getNotificationCount();

        }
    }

    @Override
    public void onBackPressed() {

        if (fragment_home_teacher != null && fragment_home_teacher.isAdded() && fragment_home_teacher.isVisible()) {
            if (userModel == null) {
                navigateToLoginActivity();
            } else {
                finish();
            }
        } else {
            displayFragmentHomeTeacher();
        }


    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}