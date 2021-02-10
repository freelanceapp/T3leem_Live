package com.t3leem_live.models;

import java.io.Serializable;

public class MessageModel implements Serializable {
    private int id;
    private String room_id;
    private String from_user_id;
    private String type;
    private String message;
    private String image;
    private String date;
    private String voice;
    private UserModel.User from_user_fk;
    private boolean isLoaded = false;
    private int progress = 0;
    private int max_duration =0;
    private boolean isImageLoaded = false;


    public MessageModel() {
    }

    public MessageModel(int id, String room_id, String from_user_id, String type, String message, String image, String date, String voice, UserModel.User from_user_fk) {
        this.id = id;
        this.room_id = room_id;
        this.from_user_id = from_user_id;
        this.type = type;
        this.message = message;
        this.image = image;
        this.date = date;
        this.voice = voice;
        this.from_user_fk = from_user_fk;
    }

    public int getId() {
        return id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public String getFrom_user_id() {
        return from_user_id;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }

    public String getVoice() {
        return voice;
    }

    public UserModel.User getFrom_user_fk() {
        return from_user_fk;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setMax_duration(int max_duration) {
        this.max_duration = max_duration;
    }

    public void setImageLoaded(boolean imageLoaded) {
        isImageLoaded = imageLoaded;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public int getProgress() {
        return progress;
    }

    public int getMax_duration() {
        return max_duration;
    }

    public boolean isImageLoaded() {
        return isImageLoaded;
    }
}
