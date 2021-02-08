package com.t3leem_live.models;

import java.io.Serializable;

public class RoomModel implements Serializable {
    private int id;
    private int room_info_id;
    private String room_status;
    private String user_type;
    private RoomFkModel room_fk;
    private String created_at;
    private UserModel.User from_user_fk;
    private UserModel.User to_user_fk;

    public int getId() {
        return id;
    }

    public String getRoom_status() {
        return room_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public RoomFkModel getRoom_fk() {
        return room_fk;
    }

    public static class RoomFkModel implements Serializable
    {
        private int id;
        private String title;
        private String desc;
        private String room_status;
        private String room_type;

        public RoomFkModel() {
        }

        public RoomFkModel(int id, String title, String desc, String room_status, String room_type) {
            this.id = id;
            this.title = title;
            this.desc = desc;
            this.room_status = room_status;
            this.room_type = room_type;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDesc() {
            return desc;
        }

        public String getRoom_status() {
            return room_status;
        }

        public String getRoom_type() {
            return room_type;
        }
    }

    public UserModel.User getFrom_user_fk() {
        return from_user_fk;
    }

    public UserModel.User getTo_user_fk() {
        return to_user_fk;
    }

    public String getUser_type() {
        return user_type;
    }

    public int getRoom_info_id() {
        return room_info_id;
    }
}
