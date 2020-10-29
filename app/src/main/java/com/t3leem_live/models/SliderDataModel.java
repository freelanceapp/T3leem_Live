package com.t3leem_live.models;

import java.io.Serializable;
import java.util.List;

public class SliderDataModel implements Serializable {
    private List<SliderModel> data;

    public List<SliderModel> getData() {
        return data;
    }
}
