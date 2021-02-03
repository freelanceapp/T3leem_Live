package com.t3leem_live.models;

import java.io.Serializable;
import java.util.List;

public class JoinCenterDataModel implements Serializable {
    private List<JoinCenterModel> data;

    public List<JoinCenterModel> getData() {
        return data;
    }
}
