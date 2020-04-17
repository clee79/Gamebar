package com.example.a4322_term_project;

import android.graphics.Bitmap;

public class TopicIcon {
    Bitmap icon;
    String topicName;


    public TopicIcon(Bitmap icon, String topicName) {
        this.icon = icon;
        this.topicName = topicName;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String name) {
        this.topicName = name;
    }

}
