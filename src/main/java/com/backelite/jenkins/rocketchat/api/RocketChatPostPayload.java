package com.backelite.jenkins.rocketchat.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gillesgrousset on 07/07/2016.
 */
public class RocketChatPostPayload implements Serializable {

    private String text;
    private String channel;
    private List<RocketChatPostAttachment> attachments = new ArrayList<>();

    public RocketChatPostPayload() {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


    public List<RocketChatPostAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<RocketChatPostAttachment> attachments) {
        this.attachments = attachments;
    }
}
