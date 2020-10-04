package com.t3leem_live.activities_fragments.activity_student_home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_student_home.fragments.Fragment_Home_Student;
import com.t3leem_live.activities_fragments.activity_student_home.fragments.Fragment_Library_Student;
import com.t3leem_live.databinding.ActivityStudentHomeBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.UserModel;
import com.t3leem_live.preferences.Preferences;

public class StudentHomeActivity extends AppCompatActivity {
    private ActivityStudentHomeBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private FragmentManager fragmentManager;
    private Fragment_Home_Student fragment_home_student;
    private Fragment_Library_Student fragment_library_student;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_home);
        initView();
        if (savedInstanceState == null) {
            displayFragmentHomeStudent();
        }

    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        setUpBottomNavigation();
    }

    private void setUpBottomNavigation() {

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
                    break;

                case 4:
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
        if (fragment_library_student.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_library_student).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_library_student, "fragment_library_student").addToBackStack("fragment_library_student").commit();

        }

        updateBottomNavigationPosition(1);
    }


}