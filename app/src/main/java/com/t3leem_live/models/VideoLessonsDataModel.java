package com.t3leem_live.models;

import java.io.Serializable;
import java.util.List;

public class VideoLessonsDataModel implements Serializable {
    private List<VideoLessonsModel> data;

    public List<VideoLessonsModel> getData() {
        return data;
    }
}
