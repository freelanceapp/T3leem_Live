package com.t3leem_live.uis.module_general.activity_chat;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.t3leem_live.R;
import com.t3leem_live.models.MessageModel;
import com.t3leem_live.models.SingleMessageDataModel;
import com.t3leem_live.remote.Api;
import com.t3leem_live.share.Common;
import com.t3leem_live.tags.Tags;

import org.greenrobot.eventbus.EventBus;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceUploadAttachment extends Service {
    private String file_uri;
    private int user_id;
    private String user_token;
    private int room_id;
    private String attachment_type;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        file_uri = intent.getStringExtra("file_uri");
        user_token = intent.getStringExtra("user_token");
        user_id = intent.getIntExtra("user_id",0);
        room_id = intent.getIntExtra("room_id",0);
        attachment_type = intent.getStringExtra("attachment_type");

        uploadAttachment(attachment_type);

        return START_STICKY;
    }

    private void uploadAttachment(String attachment_type) {

        RequestBody user_id_part = Common.getRequestBodyText(String.valueOf(user_id));
        RequestBody room_id_part = Common.getRequestBodyText(String.valueOf(room_id));
        RequestBody type_part = Common.getRequestBodyText(attachment_type);

        MultipartBody.Part file_part;
        if (attachment_type.equals("image")){
            file_part = Common.getMultiPartImage(this, Uri.parse(file_uri), "image");

        }else{
            file_part = Common.getMultiPartAudio(this, file_uri, "voice");

        }
        Api.getService(Tags.base_url).sendChatAttachment("Bearer "+user_token, room_id_part, user_id_part,type_part,file_part)
                .enqueue(new Callback<SingleMessageDataModel>() {
                    @Override
                    public void onResponse(Call<SingleMessageDataModel> call, Response<SingleMessageDataModel> response) {
                        stopSelf();
                        if (response.isSuccessful() && response.body() != null) {

                            MessageModel model = response.body().getData();
                            EventBus.getDefault().post(model);
                        } else {

                            if (response.code() == 500) {

                                Toast.makeText(ServiceUploadAttachment.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ServiceUploadAttachment.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SingleMessageDataModel> call, Throwable t) {

                        try {

                            stopSelf();

                            if (t.getMessage() != null) {
                                Log.e("msg_chat_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {

                                    Toast.makeText(ServiceUploadAttachment.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ServiceUploadAttachment.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });

        stopSelf();
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
