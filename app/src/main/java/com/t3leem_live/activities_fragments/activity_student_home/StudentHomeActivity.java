package com.t3leem_live.activities_fragments.activity_student_home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_login.LoginActivity;
import com.t3leem_live.activities_fragments.activity_student_home.fragments.Fragment_Home_Student;
import com.t3leem_live.activities_fragments.activity_student_home.fragments.Fragment_Library_Student;
import com.t3leem_live.activities_fragments.activity_student_home.fragments.Fragment_Profile_Student;
import com.t3leem_live.activities_fragments.activity_student_home.fragments.Fragment_Rate_Student;
import com.t3leem_live.databinding.ActivityStudentHomeBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.StudentRateDataModel;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;

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
    private int current_pos = 0;

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
    }

    private void setUpBottomNavigation()
    {

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
        if (fragment_home_student.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_home_student).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home_student, "fragment_home_student").addToBackStack("fragment_home_student").commit();

        }

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

        if (fragment_library_student.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_library_student).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_library_student, "fragment_library_student").addToBackStack("fragment_library_student").commit();

        }

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
        if (fragment_profile_student.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_profile_student).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile_student, "fragment_profile_student").addToBackStack("fragment_profile_student").commit();

        }

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
        if (fragment_rate_student.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_rate_student).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_rate_student, "fragment_rate_student").addToBackStack("fragment_rate_student").commit();

        }

        updateBottomNavigationPosition(3);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment:fragmentList){
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment:fragmentList){
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void logout()
    {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .logout("Bearer "+userModel.getData().getToken())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            preferences.clear(StudentHomeActivity.this);
                            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            if (notificationManager!=null){
                                notificationManager.cancel(Tags.not_tag,Tags.not_id);
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
    @Override
    public void onBackPressed()
    {
        if (fragment_home_student!=null&&fragment_home_student.isAdded()&&fragment_home_student.isVisible()){
            if (userModel==null){
                navigateToLoginActivity();
            }else {
                finish();
            }
        }else {
            displayFragmentHomeStudent();
        }



    }

    private void navigateToLoginActivity() {
        Intent  intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}