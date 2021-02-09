package com.t3leem_live.uis.module_student.activity_student_home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.iid.FirebaseInstanceId;
import com.t3leem_live.R;
import com.t3leem_live.models.NotFireModel;
import com.t3leem_live.models.NotificationCountModel;
import com.t3leem_live.models.NotificationDataModel;
import com.t3leem_live.models.StudentCenterModel;
import com.t3leem_live.uis.module_general.activity_login.LoginActivity;
import com.t3leem_live.uis.module_student.activity_notification.NotificationActivity;
import com.t3leem_live.uis.module_student.activity_student_home.fragments.Fragment_Home_Student;
import com.t3leem_live.uis.module_student.activity_student_home.fragments.Fragment_Library_Student;
import com.t3leem_live.uis.module_student.activity_student_home.fragments.Fragment_Live_Student;
import com.t3leem_live.uis.module_student.activity_student_home.fragments.Fragment_Profile_Student;
import com.t3leem_live.uis.module_student.activity_student_home.fragments.Fragment_Rate_Student;
import com.t3leem_live.databinding.ActivityStudentHomeBinding;
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
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentHomeActivity extends AppCompatActivity {
    private ActivityStudentHomeBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private FragmentManager fragmentManager;
    private Fragment_Home_Student fragment_home_student;
    private Fragment_Rate_Student fragment_rate_student;
    private Fragment_Library_Student fragment_library_student;
    private Fragment_Profile_Student fragment_profile_student;
    private Fragment_Live_Student fragment_live_student;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_home);
        initView();


    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);

        displayFragmentHomeStudent();
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
                                    Toast.makeText(StudentHomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(StudentHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(StudentHomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(StudentHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void setUpBottomNavigation() {
        binding.setTitle(getString(R.string.home));
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home), R.drawable.ic_nav_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.library), R.drawable.ic_nav_library);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.live), R.drawable.ic_nav_video_camera);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.my_rate), R.drawable.ic_nav_rate);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.profile), R.drawable.ic_nav_user);

        binding.ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        binding.ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.gray0));
        binding.ahBottomNav.setTitleTextSizeInSp(13, 13);
        binding.ahBottomNav.setForceTint(true);
        binding.ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.color1));
        binding.ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.black));

        binding.ahBottomNav.addItem(item1);
        binding.ahBottomNav.addItem(item2);
        binding.ahBottomNav.addItem(item3);
        binding.ahBottomNav.addItem(item4);
        binding.ahBottomNav.addItem(item5);


        binding.ahBottomNav.setOnTabSelectedListener((position, wasSelected) -> {
            switch (position) {
                case 0:
                    displayFragmentHomeStudent();
                    break;
                case 1:
                    displayFragmentLibraryStudent();
                    break;
                case 2:
                    Log.e("fff", "tt");
                    displayFragmentLiveStudent();
                    break;
                case 3:
                    displayFragmentRateStudent();
                    break;

                case 4:
                    displayFragmentProfileStudent();
                    break;

            }
            return false;
        });

        updateBottomNavigationPosition(0);

    }

    public void updateBottomNavigationPosition(int pos) {

        binding.ahBottomNav.setCurrentItem(pos, false);

    }


    public void displayFragmentHomeStudent() {
        if (fragment_home_student == null) {
            fragment_home_student = Fragment_Home_Student.newInstance();
        }

        if (fragment_library_student != null && fragment_library_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_library_student).commit();
        }
        if (fragment_profile_student != null && fragment_profile_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_profile_student).commit();
        }

        if (fragment_rate_student != null && fragment_rate_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_rate_student).commit();
        }

        if (fragment_live_student != null && fragment_live_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_live_student).commit();
        }

        if (fragment_home_student.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_home_student).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home_student, "fragment_home_student").commit();

        }

        binding.setTitle(getString(R.string.home));

        updateBottomNavigationPosition(0);
    }


    public void displayFragmentLibraryStudent() {
        if (fragment_library_student == null) {
            fragment_library_student = Fragment_Library_Student.newInstance();
        }

        if (fragment_home_student != null && fragment_home_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_home_student).commit();
        }
        if (fragment_profile_student != null && fragment_profile_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_profile_student).commit();
        }
        if (fragment_rate_student != null && fragment_rate_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_rate_student).commit();
        }

        if (fragment_live_student != null && fragment_live_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_live_student).commit();
        }

        if (fragment_library_student.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_library_student).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_library_student, "fragment_library_student").commit();

        }
        binding.setTitle(getString(R.string.library));

        updateBottomNavigationPosition(1);
    }


    public void displayFragmentProfileStudent() {
        if (fragment_profile_student == null) {
            fragment_profile_student = Fragment_Profile_Student.newInstance();
        }

        if (fragment_home_student != null && fragment_home_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_home_student).commit();
        }
        if (fragment_library_student != null && fragment_library_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_library_student).commit();
        }
        if (fragment_rate_student != null && fragment_rate_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_rate_student).commit();
        }
        if (fragment_live_student != null && fragment_live_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_live_student).commit();
        }

        if (fragment_profile_student.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_profile_student).commit();
            fragment_profile_student.updateUserData();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile_student, "fragment_profile_student").commit();

        }
        binding.setTitle(getString(R.string.profile));

        updateBottomNavigationPosition(4);
    }


    public void displayFragmentRateStudent() {
        if (fragment_rate_student == null) {
            fragment_rate_student = Fragment_Rate_Student.newInstance();
        }

        if (fragment_library_student != null && fragment_library_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_library_student).commit();
        }
        if (fragment_profile_student != null && fragment_profile_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_profile_student).commit();
        }

        if (fragment_home_student != null && fragment_home_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_home_student).commit();
        }
        if (fragment_live_student != null && fragment_live_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_live_student).commit();
        }
        if (fragment_rate_student.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_rate_student).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_rate_student, "fragment_rate_student").commit();

        }
        binding.setTitle(getString(R.string.my_rate));

        updateBottomNavigationPosition(3);
    }

    public void displayFragmentLiveStudent() {
        if (fragment_live_student == null) {
            fragment_live_student = Fragment_Live_Student.newInstance();
        }

        if (fragment_library_student != null && fragment_library_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_library_student).commit();
        }
        if (fragment_profile_student != null && fragment_profile_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_profile_student).commit();
        }

        if (fragment_home_student != null && fragment_home_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_home_student).commit();
        }
        if (fragment_rate_student != null && fragment_rate_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_rate_student).commit();
        }
        if (fragment_live_student.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_live_student).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_live_student, "fragment_live_student").commit();

        }

        binding.setTitle(getString(R.string.live));

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
                                        preferences.create_update_userdata(StudentHomeActivity.this, userModel);
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
                                                Toast.makeText(StudentHomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(StudentHomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(StudentHomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(StudentHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(StudentHomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(StudentHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                            preferences.clear(StudentHomeActivity.this);
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
                                Toast.makeText(StudentHomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(StudentHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(StudentHomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(StudentHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
        getNotificationCount();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onBackPressed() {
        if (fragment_home_student != null && fragment_home_student.isAdded() && fragment_home_student.isVisible()) {
            if (userModel == null) {
                navigateToLoginActivity();
            } else {
                finish();
            }
        } else {
            displayFragmentHomeStudent();
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