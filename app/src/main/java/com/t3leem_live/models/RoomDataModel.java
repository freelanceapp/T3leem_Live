package com.t3leem_live.models;

import java.util.List;

public class RoomDataModel {
    private int current_page;
    private List<RoomModel> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<RoomModel> getData() {
        return data;
    }
}
