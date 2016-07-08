package com.backelite.jenkins.rocketchat.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gillesgrousset on 07/07/2016.
 */
public class RocketChatPostAttachment implements Serializable {

    private String title;
    @SerializedName("title_link")
    private String titleLink;
    private String text;
    @SerializedName("image_url")
    private String imageURL;
    private String color;

    public RocketChatPostAttachment() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleLink() {
        return titleLink;
    }

    public void setTitleLink(String titleLink) {
        this.titleLink = titleLink;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
