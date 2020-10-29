package com.t3leem_live.models;

import java.io.Serializable;

public class SliderModel implements Serializable {
    private int id;
    private String title;
    private String desc;
    private String image;
    private String icon;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    public String getIcon() {
        return icon;
    }
}
