package com.t3leem_live.activities_fragments.activity_teacher_create_chat_group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.t3leem_live.R;
import com.t3leem_live.activities_fragments.activity_teacher_create_stream.TeacherCreateStreamActivity;
import com.t3leem_live.databinding.ActivityTeacherCreateGroupChatBinding;
import com.t3leem_live.databinding.ActivityTeacherCreateStreamBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.CreateRoomModel;
import com.t3leem_live.models.StreamDataModel;
import com.t3leem_live.models.TeacherGroupModel;
import com.t3leem_live.models.UserModel;
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

public class TeacherCreateGroupChatActivity extends AppCompatActivity {

    private ActivityTeacherCreateGroupChatBinding binding;
    private String lang = "ar";
    private Preferences preferences;
    private UserModel userModel;
    private TeacherGroupModel teacherGroupModel;
    private List<Integer> selectedStudentList = new ArrayList<>();
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_create_group_chat);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        teacherGroupModel = (TeacherGroupModel) intent.getSerializableExtra("data");
        selectedStudentList = (List<Integer>) intent.getSerializableExtra("list");

    }

    private void initView()
    {

        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(teacherGroupModel);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);


        binding.llBack.setOnClickListener(view -> {
            finish();
        });

        binding.llCreateChat.setOnClickListener(view -> {
            String title = binding.edtTitle.getText().toString();
            String description = binding.edtDescription.getText().toString();

            if (!title.isEmpty()){
                binding.edtTitle.setError(null);
                Common.CloseKeyBoard(this,binding.edtTitle);
                if (teacherGroupModel!=null){
                    createGroup(title,description);

                }else if (selectedStudentList!=null&&selectedStudentList.size()>0){
                    createStudentGroup(title,description);
                }
            }else {
                binding.edtTitle.setError(getString(R.string.field_required));



            }
        });


    }
    private void createGroup(String title, String description)
    {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .teacherCreateChatGroups("Bearer "+userModel.getData().getToken(),title,description,"Teacher_To_Student",userModel.getData().getId(),teacherGroupModel.getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            finish();
                            Toast.makeText(TeacherCreateGroupChatActivity.this, R.string.suc, Toast.LENGTH_SHORT).show();

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(TeacherCreateGroupChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }else if(response.code()==404){
                                Common.CreateDialogAlert(TeacherCreateGroupChatActivity.this,getString(R.string.no_student_to_create_group));
                            } else {
                                Toast.makeText(TeacherCreateGroupChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(TeacherCreateGroupChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TeacherCreateGroupChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }


    private void createStudentGroup(String title, String description)
    {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .teacherCreateStudentChat("Bearer "+userModel.getData().getToken(),title,description,"Teacher_To_Student",userModel.getData().getId(),selectedStudentList)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            setResult(RESULT_OK);
                            finish();
                            Toast.makeText(TeacherCreateGroupChatActivity.this, R.string.suc, Toast.LENGTH_SHORT).show();

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(TeacherCreateGroupChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }else if(response.code()==404){
                                Common.CreateDialogAlert(TeacherCreateGroupChatActivity.this,getString(R.string.no_student_to_create_group));
                            } else {
                                Toast.makeText(TeacherCreateGroupChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(TeacherCreateGroupChatActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TeacherCreateGroupChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }


}