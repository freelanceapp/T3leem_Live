package com.t3leem_live.uis.module_center_course.activity_home_center;

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
import com.t3leem_live.R;
import com.t3leem_live.databinding.ActivityCenterHomeBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;
import com.t3leem_live.uis.module_center_course.activity_home_center.fragments.Fragment_Home_Center;
import com.t3leem_live.uis.module_center_course.activity_home_center.fragments.Fragment_Library_Teacher;
import com.t3leem_live.uis.module_center_course.activity_home_center.fragments.Fragment_Profile_Teacher;
import com.t3leem_live.uis.module_general.activity_login.LoginActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CenterHomeActivity extends AppCompatActivity {
    private ActivityCenterHomeBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private FragmentManager fragmentManager;
    private Fragment_Home_Center fragment_home_center;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_center_home);
        initView();


    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        displayFragmentHomeCenter();
        setUpBottomNavigation();
    }

    private void setUpBottomNavigation() {
        binding.setTitle(getResources().getString(R.string.home));
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
                    displayFragmentHomeCenter();
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

    public void displayFragmentHomeCenter() {
        if (fragment_home_center == null) {
            fragment_home_center = Fragment_Home_Center.newInstance();
        }

        if (fragment_library_teacher != null && fragment_library_teacher.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_library_teacher).commit();
        }
        if (fragment_profile_teacher != null && fragment_profile_teacher.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_profile_teacher).commit();
        }

        if (fragment_home_center.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_home_center).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home_center, "fragment_home_center").addToBackStack("fragment_home_teacher").commit();

        }
        binding.setTitle(getString(R.string.home));

        updateBottomNavigationPosition(0);
    }


    public void displayFragmentLibraryTeacher() {
        if (fragment_library_teacher == null) {
            fragment_library_teacher = Fragment_Library_Teacher.newInstance();
        }

        if (fragment_home_center != null && fragment_home_center.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_home_center).commit();
        }
        if (fragment_profile_teacher != null && fragment_profile_teacher.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_profile_teacher).commit();
        }


        if (fragment_library_teacher.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_library_teacher).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_library_teacher, "fragment_library_teacher").addToBackStack("fragment_library_teacher").commit();

        }
        binding.setTitle(getString(R.string.library));

        updateBottomNavigationPosition(1);
    }


    public void displayFragmentProfileTeacher() {
        if (fragment_profile_teacher == null) {
            fragment_profile_teacher = Fragment_Profile_Teacher.newInstance();
        }

        if (fragment_home_center != null && fragment_home_center.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_home_center).commit();
        }
        if (fragment_library_teacher != null && fragment_library_teacher.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_library_teacher).commit();
        }

        if (fragment_profile_teacher.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_profile_teacher).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile_teacher, "fragment_profile_teacher").addToBackStack("fragment_profile_teacher").commit();

        }
        binding.setTitle(getString(R.string.profile));

        updateBottomNavigationPosition(2);
    }

    public void logout() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .logout("Bearer " + userModel.getData().getToken())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            preferences.clear(CenterHomeActivity.this);
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
                                Toast.makeText(CenterHomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CenterHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(CenterHomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CenterHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });


    }

    @Override
    public void onBackPressed() {

        if (fragment_home_center != null && fragment_home_center.isAdded() && fragment_home_center.isVisible()) {
            if (userModel == null) {
                navigateToLoginActivity();
            } else {
                finish();
            }
        } else {
            displayFragmentHomeCenter();
        }


    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}