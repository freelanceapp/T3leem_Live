package com.t3leem_live.uis.module_general.activity_chat_rooms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.t3leem_live.R;
import com.t3leem_live.uis.module_general.activity_chat.ChatActivity;
import com.t3leem_live.adapters.ChatRoomsAdapter;
import com.t3leem_live.databinding.ActivityChatRoomsBinding;
import com.t3leem_live.language.Language;
import com.t3leem_live.models.RoomDataModel;
import com.t3leem_live.models.RoomModel;
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

public class ChatRoomsActivity extends AppCompatActivity {
    private ActivityChatRoomsBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private ChatRoomsAdapter adapter;
    private List<RoomModel> roomModelList;
    private int current_page = 1;
    private boolean isLoading = false;
    private String type = "normal";


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_rooms);
        initView();
    }


    private void initView() {
        roomModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setLang(lang);
        binding.setModel(userModel);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatRoomsAdapter(roomModelList, this, userModel.getData().getUser_type());
        binding.recView.setAdapter(adapter);


        binding.llBack.setOnClickListener(view -> {
            finish();
        });

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    LinearLayoutManager manager = (LinearLayoutManager) binding.recView.getLayoutManager();
                    int last_item_pos = manager.findLastCompletelyVisibleItemPosition();
                    int total_items_count = binding.recView.getAdapter().getItemCount();
                    if (total_items_count >= 20 && last_item_pos == (total_items_count - 4) && !isLoading) {
                        int page = current_page + 1;
                        loadMore(page);
                    }
                }
            }
        });

        if (userModel.getData().getUser_type().equals("student") || userModel.getData().getUser_type().equals("teacher")) {
            type = "all";
        } else {
            type = "normal";
        }
        getRooms();

    }

    private void getRooms() {
        Api.getService(Tags.base_url).getRooms("Bearer " + userModel.getData().getToken(), "on", 20, 1, userModel.getData().getId(), userModel.getData().getUser_type(), type)
                .enqueue(new Callback<RoomDataModel>() {
                    @Override
                    public void onResponse(Call<RoomDataModel> call, Response<RoomDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                roomModelList.clear();

                                if (response.body().getData().size() > 0) {
                                    binding.tvNoConversations.setVisibility(View.GONE);
                                    roomModelList.addAll(response.body().getData());
                                    current_page = response.body().getCurrent_page();
                                } else {
                                    binding.tvNoConversations.setVisibility(View.VISIBLE);

                                }

                                adapter.notifyDataSetChanged();


                            }
                        } else {
                            binding.progBar.setVisibility(View.GONE);
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<RoomDataModel> call, Throwable t) {
                        binding.progBar.setVisibility(View.GONE);
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ChatRoomsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(ChatRoomsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void loadMore(int page) {
        adapter.notifyItemInserted(roomModelList.size() - 1);
        isLoading = true;

        Api.getService(Tags.base_url).getRooms("Bearer " + userModel.getData().getToken(), "on", 20, page, userModel.getData().getId(), userModel.getData().getUser_type(), type)
                .enqueue(new Callback<RoomDataModel>() {
                    @Override
                    public void onResponse(Call<RoomDataModel> call, Response<RoomDataModel> response) {
                        isLoading = false;
                        if (roomModelList.get(roomModelList.size() - 1) == null) {
                            roomModelList.remove(roomModelList.size() - 1);
                            adapter.notifyItemRemoved(roomModelList.size() - 1);
                        }
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getData().size() > 0) {
                                current_page = response.body().getCurrent_page();
                                int old_pos = roomModelList.size() - 1;
                                roomModelList.addAll(response.body().getData());
                                int new_pos = roomModelList.size();
                                adapter.notifyItemRangeInserted(old_pos, new_pos);

                            }
                        } else {
                            isLoading = false;
                            if (roomModelList.get(roomModelList.size() - 1) == null) {
                                roomModelList.remove(roomModelList.size() - 1);
                                adapter.notifyItemRemoved(roomModelList.size() - 1);
                            }
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<RoomDataModel> call, Throwable t) {
                        isLoading = false;
                        if (roomModelList.get(roomModelList.size() - 1) == null) {
                            roomModelList.remove(roomModelList.size() - 1);
                            adapter.notifyItemRemoved(roomModelList.size() - 1);
                        }
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ChatRoomsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(ChatRoomsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });

    }


    public void navigateToChatActivity(RoomModel model) {

        String title = "";
        String desc = "";
        if (model.getUser_type().equals("parent")) {
            desc ="";
            if (userModel.getData().getUser_type().equals("parent")) {
                title = model.getTo_user_fk().getName();
            } else {
                title = model.getFrom_user_fk().getName();

            }
        } else {
            title = model.getRoom_fk().getTitle();
            desc = model.getRoom_fk().getDesc();

        }
        RoomModel.RoomFkModel roomFkModel = new RoomModel.RoomFkModel(model.getId(), title, desc, model.getRoom_status(), model.getRoom_fk().getRoom_type());

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("data", roomFkModel);
        startActivity(intent);
    }

}