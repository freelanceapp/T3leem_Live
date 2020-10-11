package com.t3leem_live.models;

import java.io.Serializable;
import java.util.List;

public class SummaryDataModel implements Serializable {
    private List<SummaryModel> data;

    public List<SummaryModel> getData() {
        return data;
    }
}
