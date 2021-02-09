package com.t3leem_live.uis.module_parent.activity_home_parent;

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
import com.t3leem_live.uis.module_center_course.activity_home_center.CenterHomeActivity;
import com.t3leem_live.uis.module_parent.activity_home_parent.fragments.Fragment_Home_Parent;
import com.t3leem_live.uis.module_parent.activity_home_parent.fragments.Fragment_Profile_Parent;
import com.t3leem_live.uis.module_general.activity_login.LoginActivity;
import com.t3leem_live.databinding.ActivityParentHomeBinding;
import com.t3leem_live.language.Language;
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

public class ParentHomeActivity extends AppCompatActivity {
    private ActivityParentHomeBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private FragmentManager fragmentManager;
    private Fragment_Home_Parent fragment_home_parent;
    private Fragment_Profile_Parent fragment_profile_parent;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_parent_home);
        initView();


    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);

        displayFragmentHomeParent();
        if (userModel.getData().getUser_type().equals("parent")){
            setUpBottomNavigation();
            binding.ahBottomNav.setVisibility(View.VISIBLE);
            updateTokenFireBase();

        }else {
            binding.ahBottomNav.setVisibility(View.GONE);

        }
    }

    private void setUpBottomNavigation()
    {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home), R.drawable.ic_nav_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.profile), R.drawable.ic_nav_user);

        binding.ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        binding.ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.gray0));
        binding.ahBottomNav.setTitleTextSizeInSp(13, 13);
        binding.ahBottomNav.setForceTint(true);
        binding.ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.color1));
        binding.ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.black));

        binding.ahBottomNav.addItem(item1);
        binding.ahBottomNav.addItem(item2);



        binding.ahBottomNav.setOnTabSelectedListener((position, wasSelected) -> {
            switch (position) {
                case 0:
                    displayFragmentHomeParent();
                    break;
                case 1:
                    displayFragmentProfileParent();
                    break;


            }
            return false;
        });

        updateBottomNavigationPosition(0);

    }

    public void updateBottomNavigationPosition(int pos) {

        binding.ahBottomNav.setCurrentItem(pos, false);

    }


    public void displayFragmentHomeParent() {
        if (fragment_home_parent == null) {
            fragment_home_parent = Fragment_Home_Parent.newInstance();
        }

        if (fragment_profile_parent != null && fragment_profile_parent.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_profile_parent).commit();
        }


        if (fragment_home_parent.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_home_parent).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home_parent, "fragment_home_parent").commit();

        }

        updateBottomNavigationPosition(0);
    }




    public void displayFragmentProfileParent() {
        if (fragment_profile_parent == null) {
            fragment_profile_parent = Fragment_Profile_Parent.newInstance();
        }

        if (fragment_home_parent != null && fragment_home_parent.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_home_parent).commit();
        }


        if (fragment_profile_parent.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_profile_parent).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile_parent, "fragment_profile_parent").commit();

        }

        updateBottomNavigationPosition(1);
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
                                        preferences.create_update_userdata(ParentHomeActivity.this, userModel);
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
                                                Toast.makeText(ParentHomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(ParentHomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(ParentHomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ParentHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(ParentHomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ParentHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                            preferences.clear(ParentHomeActivity.this);
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
                                Toast.makeText(ParentHomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ParentHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(ParentHomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ParentHomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });


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

    @Override
    public void onBackPressed()
    {
        if (fragment_home_parent !=null&& fragment_home_parent.isAdded()&& fragment_home_parent.isVisible()){
            if (userModel==null){
                navigateToLoginActivity();
            }else {
                finish();
            }
        }else {
            displayFragmentHomeParent();
        }



    }

    private void navigateToLoginActivity() {
        Intent  intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}