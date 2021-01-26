package com.t3leem_live.models;

import java.io.Serializable;
import java.util.List;

public class CenterGroupDataModel implements Serializable {
    private List<CenterGroupModel> data;

    public List<CenterGroupModel> getData() {
        return data;
    }
}
