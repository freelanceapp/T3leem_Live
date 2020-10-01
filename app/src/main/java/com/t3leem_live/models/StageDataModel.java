package com.t3leem_live.models;

import java.io.Serializable;
import java.util.List;

public class StageDataModel implements Serializable {
    private List<StageModel> data;

    public List<StageModel> getData() {
        return data;
    }
}
