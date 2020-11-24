package com.t3leem_live.models;

import java.io.Serializable;
import java.util.List;

public class MySonsDataModel implements Serializable {
    private List<UserModel.User> data;

    public List<UserModel.User> getData() {
        return data;
    }
}
