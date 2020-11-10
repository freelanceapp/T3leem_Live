package com.t3leem_live.models;

import java.io.Serializable;

public class SettingDataModel implements Serializable {
    private Setting settings;

    public Setting getSettings() {
        return settings;
    }

    public static class Setting implements Serializable{
        private String about_app;
        private String ar_termis_condition;
        private String about_website;

        public String getAbout_app() {
            return about_app;
        }

        public String getAr_termis_condition() {
            return ar_termis_condition;
        }

        public String getAbout_website() {
            return about_website;
        }
    }
}
