package com.backelite.jenkins.rocketchat.api;

import java.io.Serializable;

/**
 * Created by gillesgrousset on 07/07/2016.
 */
public class RocketChatAPIException extends  Exception implements Serializable {

    public RocketChatAPIException(String message) {
        super(message);
    }

    public RocketChatAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public RocketChatAPIException(Throwable cause) {
        super(cause);
    }

    public RocketChatAPIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
