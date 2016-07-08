package com.backelite.jenkins.rocketchat.api;

import com.google.gson.Gson;
import us.monoid.json.JSONObject;
import us.monoid.web.Resty;

import java.io.PrintStream;
import java.net.URLEncoder;

import static us.monoid.web.Resty.*;

/**
 * Created by gillesgrousset on 07/07/2016.
 */
public class RocketChatAPIClient {

    /**
     * Webhook URL provided by the Incoming webhook decalred in Rocket.Chat
     */
    private String webhookUrl;
    private PrintStream logger;

    public RocketChatAPIClient(String webhookUrl, PrintStream logger) {
        this.webhookUrl = webhookUrl;
        this.logger = logger;
    }

    public void post(RocketChatPostPayload payload) throws RocketChatAPIException {


        Gson gson = new Gson();
        String jsonString = gson.toJson(payload);

        Resty r = new Resty();
        try {
            logger.print("Post Data: " + jsonString);
            boolean success = (Boolean) r.json(webhookUrl, content(new JSONObject(jsonString))).get("success");
            if (!success) {
                throw new RocketChatAPIException("Failed to notify Rocket.Chat");
            }

        } catch (Exception e) {
            throw new RocketChatAPIException(e);
        }

    }
}
